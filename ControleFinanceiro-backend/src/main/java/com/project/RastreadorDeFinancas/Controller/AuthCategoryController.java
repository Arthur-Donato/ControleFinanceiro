package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.Category.CreateCategoryDto;
import com.project.RastreadorDeFinancas.Dtos.Category.CategoryResponseDto;
import com.project.RastreadorDeFinancas.Exceptions.CategoryNotSavedException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Services.CategoryService;
import com.project.RastreadorDeFinancas.Services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/category")
@CrossOrigin( origins = "*")
@RequiredArgsConstructor
public class AuthCategoryController {

    @Getter
    private final CategoryService categoryService;

    @Getter
    private final UserService userService;

    @PostMapping(path = "/add/{id}")
    public ResponseEntity<CategoryResponseDto> createNewCategory(@RequestBody CreateCategoryDto createCategoryDto, @PathVariable(value = "id") UUID idUser){
        try{
            CategoryResponseDto category = this.categoryService.createNewCategory(createCategoryDto, idUser);

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
