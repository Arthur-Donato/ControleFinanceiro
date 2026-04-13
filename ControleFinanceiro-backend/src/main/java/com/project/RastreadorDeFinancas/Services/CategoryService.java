package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Controller.CategoryController;
import com.project.RastreadorDeFinancas.Dtos.Category.CategoryResponseDto;
import com.project.RastreadorDeFinancas.Dtos.User.UserResponseDto;
import com.project.RastreadorDeFinancas.Dtos.Category.CategoryUpdateDto;
import com.project.RastreadorDeFinancas.Dtos.Category.CreateCategoryDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.CategoryNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.CategoryNotSavedException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Repository.CategoryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class CategoryService {

    @Getter
    private final CategoryRepository categoryRepository;

    @Getter
    private final UserService userService;

     public CategoryResponseDto createNewCategory(@RequestBody @Validated CreateCategoryDto createCategoryDto, UUID idUser) throws UserNotFoundException, CategoryNotSavedException {

         CategoryEntity newCategory = new CategoryEntity();

         BeanUtils.copyProperties(createCategoryDto, newCategory);

         this.attributeUserEntityToNewCategory(idUser, newCategory);

         this.saveCategory(newCategory);

         return new CategoryResponseDto(newCategory);
     }

     public void attributeUserEntityToNewCategory(UUID idUser, CategoryEntity newCategory) throws UserNotFoundException{
        UserEntity user = this.userService.getOneUserByID(idUser);

        newCategory.setUserEntity(user);
     }

     public List<EntityModel<CategoryResponseDto>> getAllCategories(UUID idUser) throws UserNotFoundException{
        UserResponseDto user = this.returnUserResponseDto(idUser);

        List<CategoryEntity> categoriesList = this.categoryRepository.findAllByUserID(user.idUser());

        if(categoriesList.isEmpty()){
            throw new UserNotFoundException();
        }

        return categoriesList.stream().map(category -> {
            CategoryResponseDto categoryResponseDto = new CategoryResponseDto(category);

            EntityModel<CategoryResponseDto> resource = EntityModel.of(categoryResponseDto);

            resource.add(linkTo(methodOn(CategoryController.class).getOneCategoryById(user.idUser(), categoryResponseDto.idCateogry())).withSelfRel());

            return resource;

        }).collect(Collectors.toList());
     }

     public CategoryEntity getCategoryByID(UUID idUser, UUID idCategory) throws CategoryNotFoundException, UserNotFoundException{
         return getOneCategoryByID(idUser, idCategory);
     }

    protected CategoryEntity getOneCategoryByID(UUID idUser, UUID idCategory) throws CategoryNotFoundException, UserNotFoundException{
        UserResponseDto user = this.returnUserResponseDto(idUser);

        Optional<CategoryEntity> category = this.categoryRepository.findByUserEntityAndID(user.idUser(), idCategory);

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

     public CategoryResponseDto updateCategoryByID(UUID idUser, UUID idCategory, @RequestBody @Validated CategoryUpdateDto categoryUpdateDto) throws CategoryNotFoundException, CategoryNotSavedException, UserNotFoundException{
        CategoryEntity category = getOneCategoryByID(idUser,idCategory);

        CategoryEntity categoryAux = new CategoryEntity();

        BeanUtils.copyProperties(categoryUpdateDto, categoryAux);

        category.setName(categoryAux.getName());

        this.saveCategory(category);

        return new CategoryResponseDto(category);
     }

     public boolean saveCategory(CategoryEntity category) {
        if(this.categoryRepository.save(category).getClass() == CategoryEntity.class){
            return true;
        }
        throw new CategoryNotSavedException();
     }

     public UserResponseDto returnUserResponseDto(UUID userID) throws UserNotFoundException{
        UserEntity user = this.userService.getOneUserByID(userID);

        return new UserResponseDto(user);

     }
}
