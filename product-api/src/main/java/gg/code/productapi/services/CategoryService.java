package gg.code.productapi.services;

import gg.code.productapi.config.SuccessResponse;
import gg.code.productapi.config.exceptions.ServiceException;
import gg.code.productapi.config.exceptions.ValidationException;
import gg.code.productapi.dto.request.CategoryRequest;
import gg.code.productapi.dto.response.CategoryResponse;
import gg.code.productapi.models.Category;
import gg.code.productapi.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    public CategoryResponse save(CategoryRequest request){
        validateCategoryNameInformed(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    public CategoryResponse update(CategoryRequest request){
        if (ObjectUtils.isEmpty(request.getId())){
            throw new ValidationException("Category Id, must be informed!");
        }
        if (!categoryRepository.existsById(request.getId())){
            throw new ServiceException(HttpStatus.NOT_FOUND.value(), "Category not found");
        }
        return CategoryResponse.of(categoryRepository.save(Category.of(request)));
    }

    public Category findById(Integer id){
        if (ObjectUtils.isEmpty(id)){
            throw new ValidationException("Category id must be informed!");
        }
        return categoryRepository
                .findById(id).orElseThrow(
                        () -> new ServiceException(HttpStatus.NOT_FOUND.value(), "Category not found")
                );
    }

    public CategoryResponse findByIdResponse(Integer id){
        return CategoryResponse.of(this.findById(id));
    }

    public List<CategoryResponse> findAll(){
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> findByDescription(String description){
        if (ObjectUtils.isEmpty(description)){
            throw new ValidationException("The category description must be informed");
        }
        return categoryRepository.findByDescriptionIgnoreCaseContaining(description)
                .stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }

    public SuccessResponse delete(Integer id){
        if (ObjectUtils.isEmpty(id)){
            throw new ValidationException("Category id must be informed");
        }
        if (productService.existsByCategoryId(id)){
            throw new ServiceException(
                    HttpStatus.FORBIDDEN.value(),
                    "Products with the category id "+id+" exists, Category cannot be deleted"
            );
        }

        categoryRepository.delete(findById(id));
        return SuccessResponse.create("Category id "+id+" deleted with success");
    }

    private void validateCategoryNameInformed(CategoryRequest request) {
        if (ObjectUtils.isEmpty(request.getDescription()))
            throw new ValidationException("The category description must be informed");
    }
}
