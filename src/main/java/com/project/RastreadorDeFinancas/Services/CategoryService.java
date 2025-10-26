package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Controller.CategoryController;
import com.project.RastreadorDeFinancas.Dtos.CreateCategoryDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
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

     public CategoryEntity createNewCategory(@RequestBody @Validated CreateCategoryDto createCategoryDto, UUID idUser) throws UserNotFoundException {
         UserEntity user = userService.getOneUser(idUser);

         CategoryEntity newCategory = new CategoryEntity();

         BeanUtils.copyProperties(createCategoryDto, newCategory);

         newCategory.setUserEntity(user);

         this.categoryRepository.save(newCategory);

         return newCategory;
     }

     public List<CategoryEntity> getAllCategories(UUID idUser) throws UserNotFoundException{
        Optional<UserEntity> user = this.userRepository.findById(idUser);

        if(user.isPresent()){
            List<CategoryEntity> categoryList = this.categoryRepository.findAllByUserEntity(user.get());

            for(CategoryEntity category : categoryList){
                category.add(linkTo(methodOn(CategoryController.class).getOneCategoryById(idUser,category.getID())).withSelfRel());
            }

            return categoryList;
        }
        else{
            throw new UserNotFoundException();
        }
     }

    public CategoryEntity getOneCategory(UUID idUser, UUID idCategory) throws CategoryNotFoundException, UserNotFoundException{

        UserEntity user = this.userService.getOneUser(idUser);

        Optional<CategoryEntity> category = this.categoryRepository.findByUserEntityAndID(user, idCategory);

        if(category.isPresent()){
            category.get().add(linkTo(methodOn(CategoryController.class).getAllCategories(idUser)).withSelfRel());

            return category.get();
        }
        else{
            throw new CategoryNotFoundException();
        }
    }

     public void deleteCategory(UUID idUser, UUID idCategory) throws CategoryNotFoundException, UserNotFoundException{

         CategoryEntity category = getOneCategory(idUser, idCategory);

         this.categoryRepository.delete(category);
     }

     public CategoryEntity editCategory(UUID idUser, UUID idCategory, @RequestBody @Validated CreateCategoryDto createCategoryDto) throws CategoryNotFoundException{
        CategoryEntity category = getOneCategory(idUser,idCategory);

         CategoryEntity categoryAux = new CategoryEntity();

         BeanUtils.copyProperties(createCategoryDto, categoryAux);

         category.setName(categoryAux.getName());

         this.categoryRepository.save(category);

         return category;
     }
}
