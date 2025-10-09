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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/category/{idUser}")
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
    public ResponseEntity<Object> getOneCategory(@PathVariable(value = "id") UUID id, @PathVariable (value = "idUser") UUID idUser){
        Optional<CategoryEntity> possibleCategory = this.categoryRepository.findById(id);


        if(possibleCategory.isPresent()){
            CategoryEntity category = possibleCategory.get();

            if(category.getID().equals(idUser)){
                category.add(linkTo(methodOn(CategoryController.class).getAllCategories(idUser)).withSelfRel());

                return ResponseEntity.status(HttpStatus.OK).body(category);
            }

            return ResponseEntity.status(HttpStatus.OK).body("Essa categoria não pertence ao usuário informado");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrada nenhuma categoria com esse ID");
    }

    @GetMapping
    public ResponseEntity<Object> getAllCategories(@PathVariable (value = "idUser") UUID idUser){
        if(this.categoryRepository.count() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma categoria registrada para esse usuário");
        }

        List<CategoryEntity> categoryList = this.categoryRepository.findAll();
        List<CategoryEntity> finalList = new ArrayList<>();

        for(CategoryEntity category : categoryList){

             if(category.getUserEntity().getID().equals(idUser)){
                 finalList.add(category);

                 category.add(linkTo(methodOn(CategoryController.class).getOneCategory(category.getID(), idUser)).withSelfRel());
             }

        }

        if(finalList.size() != 0){
            return ResponseEntity.status(HttpStatus.OK).body(finalList);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn`t any category for this user");

    }


    @PostMapping
    public ResponseEntity<CategoryEntity> postCategory(@PathVariable (value = "idUser") UUID id, @RequestBody @Validated CategoryRecordDto categoryRecordDto){

        CategoryEntity categoryEntity = new CategoryEntity();

        BeanUtils.copyProperties(categoryRecordDto, categoryEntity);

        categoryEntity.setUserEntity(this.userRepository.getById(id));


        return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryRepository.save(categoryEntity));
    }

    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<Object> deleteCategory(@PathVariable (value = "idUser") UUID idUser, @PathVariable (value = "id") UUID id ){
        Optional<CategoryEntity> possibleCategory = this.categoryRepository.findById(id);

        if(possibleCategory.isPresent()){
            CategoryEntity category = possibleCategory.get();

            if(category.getUserEntity().getID().equals(idUser)){

                this.categoryRepository.deleteById(id);

                return ResponseEntity.status(HttpStatus.OK).body("The category was sucessfuly deleted");
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This category do not belong to this user");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There isn`t a category with this ID");
    }
}
