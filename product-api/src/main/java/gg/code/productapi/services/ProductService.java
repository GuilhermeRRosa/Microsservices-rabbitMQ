package gg.code.productapi.services;

import gg.code.productapi.clients.SalesClient;
import gg.code.productapi.config.SuccessResponse;
import gg.code.productapi.config.exceptions.ServiceException;
import gg.code.productapi.config.exceptions.ValidationException;
import gg.code.productapi.dto.rabbit.ProductQuantityDTO;
import gg.code.productapi.dto.rabbit.ProductStockDTO;
import gg.code.productapi.dto.rabbit.SalesConfirmationDTO;
import gg.code.productapi.dto.request.ProductCheckStockRequest;
import gg.code.productapi.dto.request.ProductRequest;
import gg.code.productapi.dto.response.CategoryResponse;
import gg.code.productapi.dto.response.ProductResponse;
import gg.code.productapi.dto.response.ProductSalesResponse;
import gg.code.productapi.enums.SalesStatus;
import gg.code.productapi.models.Product;
import gg.code.productapi.rabbitmq.SalesConfirmationSender;
import gg.code.productapi.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private static final Integer ZERO = 0;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SalesConfirmationSender salesConfirmationSender;

    @Autowired
    private SalesClient salesClient;

    public ProductResponse save(ProductRequest request){
        verifyProductData(request);
        var category = categoryService.findById(request.getCategory_id());
        var supplier = supplierService.findById(request.getSupplier_id());
        var product = productRepository.save(Product.of(request, category, supplier));
        return ProductResponse.of(product);
    }

    public ProductResponse update(ProductRequest request){
        verifyIfProductExists(request);
        verifyProductData(request);
        var category = categoryService.findById(request.getCategory_id());
        var supplier = supplierService.findById(request.getSupplier_id());
        var product = Product.of(request, category, supplier);
        product.setId(request.getId());
        var updatedProduct = productRepository.save(product);
        return ProductResponse.of(updatedProduct);
    }

    public Product findById(Integer id){
        if (ObjectUtils.isEmpty(id) || id < ZERO){
            throw new ValidationException("Id must be provided and cannot be less than zero");
        }

        return productRepository.findById(id).orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "Product not found"));
    }

    public ProductResponse findByIdResponse(Integer id){
        return ProductResponse.of(findById(id));
    }

    public List<ProductResponse> findByName(String name){
        return productRepository.findByNameIgnoreCaseContaining(name)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByCategoryId(Integer id){
        return productRepository.findByCategoryId(id)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer id){
        return productRepository.findBySupplierId(id)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public boolean existsByCategoryId(Integer id){
        return productRepository.existsByCategoryId(id);
    }

    public boolean existsBySupplierId(Integer id){
        return productRepository.existsBySupplierId(id);
    }

    public List<ProductResponse> findAll(){
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public SuccessResponse delete(Integer id){
        if (!productRepository.existsById(id)){
            throw new ServiceException(HttpStatus.NOT_FOUND.value(), "Product not found");
        }
        productRepository.deleteById(id);
        return SuccessResponse.create("product deleted successfully");
    }

    public void updateProductStock(ProductStockDTO product){
        try {
            validateProductStock(product);
            updateStock(product);
        } catch (Exception ex){
            log.error("Error while trying to update stock for message with error: {}", ex.getMessage(), ex);
            salesConfirmationSender.sendSalesConfirmationMessage(
                    new SalesConfirmationDTO(product.getSalesId(), SalesStatus.REJECTED)
            );
        }
    }

    @Transactional
    private void updateStock(ProductStockDTO product){
        var productsForUpdate = new ArrayList<Product>();
        product.getProducts()
                .forEach(salesProduct -> {
                    var existingProduct = findById(salesProduct.getProductId());
                    validateProductQuantity(salesProduct, existingProduct);
                    existingProduct.updateStock(salesProduct.getQuantity());
                    productsForUpdate.add(existingProduct);
                });
        if (!ObjectUtils.isEmpty(productsForUpdate)){
            productRepository.saveAll(productsForUpdate);
            var approvedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.APPROVED);
            salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);
        }
    }

    private void validateProductQuantity(ProductQuantityDTO salesProduct, Product existingProduct){
        if(salesProduct.getQuantity() > existingProduct.getQuantityAvailable()){
            throw new ValidationException(
                    String.format("The product %s is out of stock", existingProduct.getId())
            );
        }
    }

    @Transactional
    private void validateProductStock(ProductStockDTO productStockDTO){
        if(ObjectUtils.isEmpty(productStockDTO.getSalesId())){
            throw new ValidationException("The sales id must be informed");
        }

        if(ObjectUtils.isEmpty(productStockDTO.getProducts())){
            throw new ValidationException("The sale's products must be informed");
        }

        productStockDTO.getProducts()
                .forEach(salesProduct -> {
                    if(ObjectUtils.isEmpty(salesProduct.getQuantity())
                    || ObjectUtils.isEmpty((salesProduct.getProductId()))){
                        throw new ValidationException("The product id and the quantity must be informed");
                    }
                });
    }

    private void verifyIfProductExists(ProductRequest request){
        if(ObjectUtils.isEmpty(request.getId())){
            throw new ValidationException("Product id must be informed");
        }

        if (!productRepository.existsById(request.getId())){
            throw new ServiceException(HttpStatus.NOT_FOUND.value(), "Product not found");
        }
    }

    private void verifyProductData(ProductRequest request){
        if (ObjectUtils.isEmpty(request.getName())){
            throw new ValidationException("Product Name must be informed");
        }

        if (ObjectUtils.isEmpty(request.getCategory_id()) || ObjectUtils.isEmpty(request.getSupplier_id())){
            throw new ValidationException("Product category and supplier must be informed");
        }

        if (ObjectUtils.isEmpty(request.getQuantityAvailable())){
            throw new ValidationException("Product quantity must be informed");
        }

        if (request.getQuantityAvailable() < ZERO) {
            throw new ValidationException("the product quantity should not be less than zero");
        }

    }

    public ProductSalesResponse findProductSales(Integer id){
        var product = findById(id);
        try {
            var sales = salesClient.findSalesByProductId(id).orElseThrow(
                    () -> new ServiceException(HttpStatus.NOT_FOUND.value(), "the sales for product "+id+" was not found")
            );
            return ProductSalesResponse.of(product, sales.getSalesId());
        } catch (Exception ex) {
            throw new ValidationException("There was an error trying to get the product's sales.");
        }
    }

    public SuccessResponse checkProductsStock(ProductCheckStockRequest request){
        if (ObjectUtils.isEmpty(request)){
            throw new ValidationException("The request data and products must be informed");
        }

        request
                .getProducts()
                .forEach(this::validateStock);
        return SuccessResponse.create("The stock is ok!");
    }

    private void validateStock(ProductQuantityDTO productQuantity){
        if (ObjectUtils.isEmpty(productQuantity.getProductId()) || ObjectUtils.isEmpty(productQuantity.getQuantity())) {
            throw new ValidationException("Product ID and quantity must be informed");
        }
        var product = findById(productQuantity.getProductId());
        if(productQuantity.getQuantity() > product.getQuantityAvailable()){
            throw new ValidationException(String.format("The product %s is out of stock", product.getId()));
        }
    }

}
