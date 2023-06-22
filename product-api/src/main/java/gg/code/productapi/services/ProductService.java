package gg.code.productapi.services;

import gg.code.productapi.config.SuccessResponse;
import gg.code.productapi.config.exceptions.ServiceException;
import gg.code.productapi.config.exceptions.ValidationException;
import gg.code.productapi.dto.request.ProductRequest;
import gg.code.productapi.dto.response.CategoryResponse;
import gg.code.productapi.dto.response.ProductResponse;
import gg.code.productapi.models.Product;
import gg.code.productapi.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Integer ZERO = 0;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

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

}
