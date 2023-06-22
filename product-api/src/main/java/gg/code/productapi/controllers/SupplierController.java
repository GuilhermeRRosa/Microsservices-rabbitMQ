package gg.code.productapi.controllers;

import gg.code.productapi.config.SuccessResponse;
import gg.code.productapi.dto.request.SupplierRequest;
import gg.code.productapi.dto.response.SupplierResponse;
import gg.code.productapi.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public SupplierResponse save(@RequestBody SupplierRequest request){
        return supplierService.save(request);
    }

    @PutMapping
    public SupplierResponse update(@RequestBody SupplierRequest request){
        return supplierService.update(request);
    }

    @GetMapping("{id}")
    public SupplierResponse findById(@PathVariable Integer id) { return supplierService.findByIdResponse(id); }

    @GetMapping("/name/{name}")
    public List<SupplierResponse> findByName(@PathVariable String name) { return supplierService.findByName(name); }

    @GetMapping
    public List<SupplierResponse> findAll() { return supplierService.findAll(); }

    @DeleteMapping("/{id}")
    public SuccessResponse delete(@PathVariable Integer id) { return supplierService.delete(id); }

}
