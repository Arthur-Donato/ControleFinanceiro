package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.CategoryRecordDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Repository.CategoryRepository;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/category")
public class CategoryController {

    @Setter
    @Getter
    private CategoryRepository categoryRepository;

    @Setter
    @Getter
    private UserRepository userRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository, UserRepository userRepository){
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getOneCategory(@PathVariable(value = "id") UUID id){
        Optional<CategoryEntity> possibleCategory = this.categoryRepository.findById(id);

        if(possibleCategory.isPresent()){
            CategoryEntity category = possibleCategory.get();

            category.add(linkTo(methodOn(CategoryController.class).getAllCategories()).withSelfRel());

            return ResponseEntity.status(HttpStatus.OK).body(category);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrada nenhuma categoria com esse ID");
    }

    @GetMapping
    public ResponseEntity<Object> getAllCategories(){
        if(this.categoryRepository.count() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma categoria registrada para esse usuário");
        }

        List<CategoryEntity> categoryList = this.categoryRepository.findAll();

        for(CategoryEntity category : categoryList){
            category.add(linkTo(methodOn(CategoryController.class).getOneCategory(category.getID())).withSelfRel());
        }

        return ResponseEntity.status(HttpStatus.OK).body(categoryList);
    }


    @PostMapping(path = "/{id}")
    public ResponseEntity<CategoryEntity> postCategory(@PathVariable (value = "id") UUID id, @RequestBody @Validated CategoryRecordDto categoryRecordDto){

        CategoryEntity categoryEntity = new CategoryEntity();

        BeanUtils.copyProperties(categoryRecordDto, categoryEntity);

        categoryEntity.setUserEntity(this.userRepository.getById(id));


        return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryRepository.save(categoryEntity));


    }
}
