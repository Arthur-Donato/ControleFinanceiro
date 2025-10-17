package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Controller.CategoryController;
import com.project.RastreadorDeFinancas.Dtos.CategoryRecordDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.CategoryDontBelongToThisUserException;
import com.project.RastreadorDeFinancas.Exceptions.CategoryNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Repository.CategoryRepository;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CategoryService {

    @Setter
    @Getter
    private UserRepository userRepository;

    @Setter
    @Getter
    private CategoryRepository categoryRepository;


    @Getter
    private final UserService userService = new UserService(userRepository);


    @Autowired
    public CategoryService(UserRepository userRepository, CategoryRepository categoryRepository){
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

     public CategoryEntity createNewCategory(@RequestBody @Validated CategoryRecordDto categoryRecordDto, UUID idUser) throws UserNotFoundException {
         UserEntity user = userService.verifyAndReturnUser(idUser);

         CategoryEntity newCategory = new CategoryEntity();

         BeanUtils.copyProperties(categoryRecordDto, newCategory);

         newCategory.setUserEntity(user);

         this.categoryRepository.save(newCategory);

         return newCategory;
     }

     public List<CategoryEntity> getAllCategories(UUID idUser){
        List<CategoryEntity> categoryList = this.categoryRepository.findAll();
        List<CategoryEntity> finalList = new ArrayList<>();

        if(!categoryList.isEmpty()){
            for(CategoryEntity category : categoryList){
                if(category.getUserEntity().getID().equals(idUser)){
                    category.add(linkTo(methodOn(CategoryController.class).getOneCategory(category.getID(), category.getUserEntity().getID())).withSelfRel());

                    finalList.add(category);
                }
            }
        }

        return finalList;
     }

    public CategoryEntity getOneCategory(UUID idUser, UUID idCategory) throws CategoryNotFoundException, CategoryDontBelongToThisUserException{
        Optional<CategoryEntity> category = this.categoryRepository.findById(idCategory);

        if (category.isPresent()) {
            if(category.get().getUserEntity().getID().equals(idUser)){
                return category.get();
            }
            else{
                throw new CategoryDontBelongToThisUserException();
            }
        }
        else{
            throw new CategoryNotFoundException();
        }
    }

     public void deleteCategory(UUID idUser, UUID idCategory) throws CategoryNotFoundException, CategoryDontBelongToThisUserException{

         CategoryEntity category = getOneCategory(idUser, idCategory);

         this.categoryRepository.delete(category);
     }

     public CategoryEntity editCategory(UUID idUser, UUID idCategory, @RequestBody @Validated CategoryRecordDto categoryRecordDto) throws CategoryNotFoundException, CategoryDontBelongToThisUserException{
        CategoryEntity category = getOneCategory(idUser,idCategory);

         CategoryEntity categoryAux = new CategoryEntity();

         BeanUtils.copyProperties(categoryRecordDto, categoryAux);

         category.setName(categoryAux.getName());

         this.categoryRepository.save(category);

         return category;
     }
}
