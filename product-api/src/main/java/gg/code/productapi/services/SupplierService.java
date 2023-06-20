package gg.code.productapi.services;

import gg.code.productapi.config.exceptions.ServiceException;
import gg.code.productapi.config.exceptions.ValidationException;
import gg.code.productapi.dto.request.CategoryRequest;
import gg.code.productapi.dto.request.SupplierRequest;
import gg.code.productapi.dto.response.CategoryResponse;
import gg.code.productapi.dto.response.SupplierResponse;
import gg.code.productapi.models.Supplier;
import gg.code.productapi.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public Supplier findById(Integer id){
        return supplierRepository.findById(id).orElseThrow(
                        () -> new ServiceException(HttpStatus.NOT_FOUND.value(), "Supplier not found")
                );
    }

    public SupplierResponse save(SupplierRequest request){
        validateSupplierName(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    public void validateSupplierName(SupplierRequest request){
        if (ObjectUtils.isEmpty(request.getName()))
            throw new ValidationException("Supplier name must be informed");
    }
}
