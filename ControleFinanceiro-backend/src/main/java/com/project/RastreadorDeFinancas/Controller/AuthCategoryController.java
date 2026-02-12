package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.CreateCategoryDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Exceptions.CategoryNotSavedException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Repository.CategoryRepository;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import com.project.RastreadorDeFinancas.Services.CategoryService;
import com.project.RastreadorDeFinancas.Services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/category")
@CrossOrigin( origins = "*")
public class AuthCategoryController {

    @Getter
    @Setter
    private CategoryService categoryService;

    @Setter
    @Getter
    private UserService userService;

    @Autowired
    public AuthCategoryController(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryService = new CategoryService(userRepository, categoryRepository);

    }

    @PostMapping(path = "/add/{id}")
    public ResponseEntity<CategoryEntity> createNewCategory(@RequestBody CreateCategoryDto createCategoryDto, @PathVariable(value = "id") UUID idUser){
        try{
            CategoryEntity category = this.categoryService.createNewCategory(createCategoryDto, idUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch(UserNotFoundException | CategoryNotSavedException e ){
            if(e.getClass() == UserNotFoundException.class){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else{
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }
    }
}
