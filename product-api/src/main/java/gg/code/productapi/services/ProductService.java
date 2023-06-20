package gg.code.productapi.services;

import gg.code.productapi.config.exceptions.ValidationException;
import gg.code.productapi.dto.request.ProductRequest;
import gg.code.productapi.dto.response.CategoryResponse;
import gg.code.productapi.dto.response.ProductResponse;
import gg.code.productapi.models.Product;
import gg.code.productapi.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
