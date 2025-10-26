package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.CategoryResponseDto;
import com.project.RastreadorDeFinancas.Dtos.CreateCategoryDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Exceptions.CategoryNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Repository.CategoryRepository;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import com.project.RastreadorDeFinancas.Services.CategoryService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/category/{idUser}")
public class CategoryController {

    @Setter
    @Getter
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository, UserRepository userRepository){
        this.categoryService = new CategoryService(userRepository, categoryRepository);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> postCategory(@PathVariable (value = "idUser") UUID idUser, @RequestBody @Validated CreateCategoryDto createCategoryDto){
        try{
            CategoryEntity category = categoryService.createNewCategory(createCategoryDto, idUser);

            CategoryResponseDto categoryResponseDto = new CategoryResponseDto(category);

            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto);
        }
        catch(UserNotFoundException e ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CategoryResponseDto> getOneCategoryById(@PathVariable(value = "idUser") UUID idUser, @PathVariable (value = "id") UUID id){
        try{
            CategoryEntity category = categoryService.getOneCategory(idUser, id);

            CategoryResponseDto categoryResponseDto = new CategoryResponseDto(category);

            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto);
        }
        catch(CategoryNotFoundException | UserNotFoundException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(@PathVariable (value = "idUser") UUID idUser){
        try{
            List<CategoryResponseDto> categoryList = this.categoryService.getAllCategories(idUser).stream().map(CategoryResponseDto::new).toList();

            return ResponseEntity.status(HttpStatus.OK).body(categoryList);
        } catch(UserNotFoundException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<CategoryResponseDto> deleteCategory(@PathVariable (value = "idUser") UUID idUser, @PathVariable (value = "id") UUID id ){
        try{
            categoryService.deleteCategory(idUser, id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch(CategoryNotFoundException | UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(path =  "/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable (value = "idUser") UUID idUser, @PathVariable (value = "id") UUID id, CreateCategoryDto createCategoryDto){
        try{
            CategoryEntity category = categoryService.editCategory(idUser, id, createCategoryDto);

            CategoryResponseDto categoryResponseDto = new CategoryResponseDto(category);

            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto);
        }
        catch(CategoryNotFoundException | UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
