package gg.code.productapi.controllers;

import gg.code.productapi.config.SuccessResponse;
import gg.code.productapi.dto.request.CategoryRequest;
import gg.code.productapi.dto.response.CategoryResponse;
import gg.code.productapi.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CategoryResponse save(@RequestBody CategoryRequest request){
        return categoryService.save(request);
    }

    @PutMapping
    public CategoryResponse update(@RequestBody CategoryRequest request){
        return categoryService.update(request);
    }

    @GetMapping
    public List<CategoryResponse> findAll(){
        return categoryService.findAll();
    }

    @GetMapping("{id}")
    public CategoryResponse findById(@PathVariable Integer id){
        return categoryService.findByIdResponse(id);
    }

    @GetMapping("/description/{description}")
    public List<CategoryResponse> findByDescription(@PathVariable String description){
        return categoryService.findByDescription(description);
    }

    @DeleteMapping("/{id}")
    public SuccessResponse delete(@PathVariable Integer id) { return categoryService.delete(id); }

}
