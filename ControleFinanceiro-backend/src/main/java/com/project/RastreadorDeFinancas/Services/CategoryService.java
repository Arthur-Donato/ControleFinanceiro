package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Controller.CategoryController;
import com.project.RastreadorDeFinancas.Dtos.Update.CategoryUpdateDto;
import com.project.RastreadorDeFinancas.Dtos.Create.CreateCategoryDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.CategoryNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.CategoryNotSavedException;
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

    @Setter
    @Getter
    private UserService userService;

    @Autowired
    public CategoryService(UserRepository userRepository, CategoryRepository categoryRepository){
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.userService = new UserService(userRepository);
    }

     public CategoryEntity createNewCategory(@RequestBody @Validated CreateCategoryDto createCategoryDto, UUID idUser) throws UserNotFoundException, CategoryNotSavedException {

         CategoryEntity newCategory = new CategoryEntity();

         BeanUtils.copyProperties(createCategoryDto, newCategory);

         this.attributeUserEntityToNewCategory(idUser, newCategory);

         this.saveCategory(newCategory);

         return newCategory;
     }

     public void attributeUserEntityToNewCategory(UUID idUser, CategoryEntity newCategory) throws UserNotFoundException{
        UserEntity user = this.userService.getOneUserByID(idUser);

        newCategory.setUserEntity(user);
     }

     public List<CategoryEntity> getAllCategories(UUID idUser) throws UserNotFoundException{
        UserEntity user = this.userService.getOneUserByID(idUser);

        List<CategoryEntity> categoriesList = this.categoryRepository.findAllByUserID(user.getID());

        if(!categoriesList.isEmpty()){
            for(CategoryEntity category : categoriesList){
                category.add(linkTo(methodOn(CategoryController.class).getOneCategoryById(idUser, category.getID())).withSelfRel());
            }

            return categoriesList;
        }else{
            throw new CategoryNotFoundException();
        }
     }

    public CategoryEntity getOneCategoryByID(UUID idUser, UUID idCategory) throws CategoryNotFoundException, UserNotFoundException{
        UserEntity user = this.userService.getOneUserByID(idUser);

        Optional<CategoryEntity> category = this.categoryRepository.findByUserEntityAndID(user.getID(), idCategory);

        if(category.isPresent()){
            category.get().add(linkTo(methodOn(CategoryController.class).getAllCategories(idUser)).withSelfRel());

            return category.get();
        }
        else{
            throw new CategoryNotFoundException();
        }
    }

     public void deleteCategoryByID(UUID idUser, UUID idCategory) throws CategoryNotFoundException, UserNotFoundException{
         CategoryEntity category = getOneCategoryByID(idUser, idCategory);

         this.categoryRepository.delete(category);
     }

     public CategoryEntity updateCategoryByID(UUID idUser, UUID idCategory, @RequestBody @Validated CategoryUpdateDto categoryUpdateDto) throws CategoryNotFoundException, CategoryNotSavedException, UserNotFoundException{
        CategoryEntity category = getOneCategoryByID(idUser,idCategory);

        CategoryEntity categoryAux = new CategoryEntity();

        BeanUtils.copyProperties(categoryUpdateDto, categoryAux);

        category.setName(categoryAux.getName());

        this.saveCategory(category);

        return category;
     }

     public boolean saveCategory(CategoryEntity category) {
        if(this.categoryRepository.save(category).getClass() == CategoryEntity.class){
            return true;
        }
        throw new CategoryNotSavedException();
     }
}
