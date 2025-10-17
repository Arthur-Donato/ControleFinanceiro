package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.CategoryRecordDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Exceptions.CategoryDontBelongToThisUserException;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getOneCategory(@PathVariable(value = "id") UUID id, @PathVariable (value = "idUser") UUID idUser){

        try{
            CategoryEntity category = categoryService.getOneCategory(idUser, id);

            category.add(linkTo(methodOn(CategoryController.class).getAllCategories(idUser)).withSelfRel());

            return ResponseEntity.status(HttpStatus.OK).body(category);
        }
        catch(CategoryNotFoundException | CategoryDontBelongToThisUserException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @GetMapping
    public ResponseEntity<Object> getAllCategories(@PathVariable (value = "idUser") UUID idUser){
        List<CategoryEntity>  categoryList = categoryService.getAllCategories(idUser);

        return ResponseEntity.status(HttpStatus.OK).body(categoryList);
    }


    @PostMapping
    public ResponseEntity<CategoryEntity> postCategory(@PathVariable (value = "idUser") UUID idUser, @RequestBody @Validated CategoryRecordDto categoryRecordDto){
        try{
            CategoryEntity category = categoryService.createNewCategory(categoryRecordDto, idUser);

            return ResponseEntity.status(HttpStatus.OK).body(category);
        }
        catch(UserNotFoundException e ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<Object> deleteCategory(@PathVariable (value = "idUser") UUID idUser, @PathVariable (value = "id") UUID id ){
        try{
            categoryService.deleteCategory(idUser, id);

            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch(CategoryNotFoundException | CategoryDontBelongToThisUserException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(path =  "/{id}")
    public ResponseEntity<Object> editCategory(@PathVariable (value = "idUser") UUID idUser, @PathVariable (value = "id") UUID id, CategoryRecordDto categoryRecordDto){
        try{
            CategoryEntity category = categoryService.editCategory(idUser, id, categoryRecordDto);

            return ResponseEntity.status(HttpStatus.OK).body(category);
        }
        catch(CategoryNotFoundException | CategoryDontBelongToThisUserException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
