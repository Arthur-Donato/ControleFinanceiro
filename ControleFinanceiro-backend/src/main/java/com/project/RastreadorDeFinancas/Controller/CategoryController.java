package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.Category.CategoryResponseDto;
import com.project.RastreadorDeFinancas.Dtos.Category.CategoryUpdateDto;
import com.project.RastreadorDeFinancas.Dtos.Category.CreateCategoryDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Exceptions.CategoryNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Services.CategoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/category/{idUser}")
@RequiredArgsConstructor
public class CategoryController {

    @Getter
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> postCategory(@PathVariable (value = "idUser") UUID idUser, @RequestBody @Validated CreateCategoryDto createCategoryDto){
        try{
            CategoryResponseDto category = categoryService.createNewCategory(createCategoryDto, idUser);

            return ResponseEntity.status(HttpStatus.OK).body(category);
        }
        catch(UserNotFoundException e ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CategoryResponseDto> getOneCategoryById(@PathVariable(value = "idUser") UUID idUser, @PathVariable (value = "id") UUID id){
        try{
            CategoryEntity category = categoryService.getCategoryByID(idUser, id);

            CategoryResponseDto categoryResponseDto = new CategoryResponseDto(category);

            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto);
        }
        catch(CategoryNotFoundException | UserNotFoundException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<CategoryResponseDto>>> getAllCategories(@PathVariable (value = "idUser") UUID idUser){
        try{
            List<EntityModel<CategoryResponseDto>> categoryList = this.categoryService.getAllCategories(idUser);

            return ResponseEntity.status(HttpStatus.OK).body(categoryList);
        } catch(UserNotFoundException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CategoryResponseDto> deleteCategory(@PathVariable (value = "idUser") UUID idUser, @PathVariable (value = "id") UUID id ){
        try{
            categoryService.deleteCategoryByID(idUser, id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch(CategoryNotFoundException | UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(path =  "/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable (value = "idUser") UUID idUser, @PathVariable (value = "id") UUID id, @RequestBody @Validated CategoryUpdateDto categoryUpdateDto){
        try{
            CategoryResponseDto category = categoryService.updateCategoryByID(idUser, id, categoryUpdateDto);

            return ResponseEntity.status(HttpStatus.OK).body(category);
        }
        catch(CategoryNotFoundException | UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
