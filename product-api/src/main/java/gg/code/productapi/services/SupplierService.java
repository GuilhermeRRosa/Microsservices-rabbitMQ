package gg.code.productapi.services;

import gg.code.productapi.config.SuccessResponse;
import gg.code.productapi.config.exceptions.ServiceException;
import gg.code.productapi.config.exceptions.ValidationException;
import gg.code.productapi.dto.request.CategoryRequest;
import gg.code.productapi.dto.request.SupplierRequest;
import gg.code.productapi.dto.response.CategoryResponse;
import gg.code.productapi.dto.response.SupplierResponse;
import gg.code.productapi.models.Category;
import gg.code.productapi.models.Supplier;
import gg.code.productapi.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductService productService;

    public Supplier findById(Integer id){
        return supplierRepository.findById(id).orElseThrow(
                        () -> new ServiceException(HttpStatus.NOT_FOUND.value(), "Supplier not found")
                );
    }

    public List<SupplierResponse> findAll() {
        return supplierRepository.findAll()
                .stream()
                .map(SupplierResponse::of)
                .collect(Collectors.toList());
    }

    public SupplierResponse findByIdResponse(Integer id){
        return SupplierResponse.of(findById(id));
    }

    public List<SupplierResponse> findByName(String name){
        return supplierRepository.findByNameIgnoreCaseContaining(name)
                .stream()
                .map(SupplierResponse::of)
                .collect(Collectors.toList());
    }

    public SupplierResponse save(SupplierRequest request){
        validateSupplierName(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    public SupplierResponse update(SupplierRequest request){
        if (ObjectUtils.isEmpty(request.getId())){
            throw new ValidationException("Supplier Id, must be informed!");
        }
        if (!supplierRepository.existsById(request.getId())){
            throw new ServiceException(HttpStatus.NOT_FOUND.value(), "Supplier not found");
        }
        return SupplierResponse.of(supplierRepository.save(Supplier.of(request)));
    }

    public SuccessResponse delete(Integer id){
        if (ObjectUtils.isEmpty(id)){
           throw new ValidationException("Supplier id must be informed");
        }
        if (productService.existsBySupplierId(id)){
            throw new ServiceException(
                    HttpStatus.FORBIDDEN.value(),
                    "Products with the supplier id "+id+" exists, Supplier cannot be deleted"
            );
        }

        supplierRepository.delete(findById(id));
        return SuccessResponse.create("Supplier id "+id+" deleted with success");
    }

    public void validateSupplierName(SupplierRequest request){
        if (ObjectUtils.isEmpty(request.getName()))
            throw new ValidationException("Supplier name must be informed");
    }
}
