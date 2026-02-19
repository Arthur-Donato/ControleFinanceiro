package com.project.RastreadorDeFinancas.Service;

import com.project.RastreadorDeFinancas.Dtos.Update.CategoryUpdateDto;
import com.project.RastreadorDeFinancas.Dtos.Create.CreateCategoryDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.CategoryNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.CategoryNotSavedException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Repository.CategoryRepository;
import com.project.RastreadorDeFinancas.Services.CategoryService;
import com.project.RastreadorDeFinancas.Services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private UserService userService;

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryEntity category;

    private UUID id;

    private UserEntity user;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        category = new CategoryEntity("Games");
        id = UUID.randomUUID();

        user = new UserEntity("12398712", "Arthur", "arthur@", "123");
        user.setID(id);

        category.setID(id);
        category.setUserEntity(user);
    }

    @Test
    public void saveCategory_ReturnCategory(){
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(category);

        boolean finalResponse = categoryService.saveCategory(category);

        Assertions.assertTrue(finalResponse);
    }

    @Test
    public void saveCategory_ThrowsCategoryNotSavedException(){
        CategoryEntity categorySubClass = new CategoryEntity(){};

        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(categorySubClass);

        Assertions.assertThrows(CategoryNotSavedException.class, () -> categoryService.saveCategory(categorySubClass));
    }

    @Test
    public void saveCategory_ThrowNullPointerException(){

        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () -> categoryService.saveCategory(category));
    }

    @Test
    public void attributeUserEntityToNewCategory_ReturnSomething(){

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(new UserEntity("2034982", "Arthur", "arhutr@", "123"));

        categoryService.attributeUserEntityToNewCategory(id, category);

        verify(userService, times(1)).getOneUserByID(id);
    }

    @Test
    public void attribuiteUserEntityToNewCategory_ThrowsUserNotFoundException(){

        when(userService.getOneUserByID(any(UUID.class))).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class, () -> categoryService.attributeUserEntityToNewCategory(id, category));

        verify(userService, times(1)).getOneUserByID(id);
    }

    @Test
    public void attribuiteUserEntityToNewCategory_ThrowNullPointerException(){
        UserEntity user = new UserEntity("12398712", "Arthur", "arthur@", "123");

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);

        Assertions.assertThrows(NullPointerException.class, () -> categoryService.attributeUserEntityToNewCategory(id, null));

        verify(userService, times(1)).getOneUserByID(id);
    }

    @Test
    public void createNewCategory_ReturnNewCategory(){
        //ARRANGE
        CreateCategoryDto dto = new CreateCategoryDto("Games");


        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(category);
        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        //ACT

        CategoryEntity newCategory = categoryService.createNewCategory(dto, id);
        newCategory.setID(id);
        newCategory.setUserEntity(user);

        //ASSERTIONS

        //Assertions.assertNotNull(newCategory);
        Assertions.assertEquals(newCategory, category);

        verify(categoryRepository, times(1)).save(category);
        verify(userService, times(1)).getOneUserByID(id);
    }

    @Test
    public void createNewCategory_ThrowsCategoryNotSavedException(){

        CreateCategoryDto dto = new CreateCategoryDto("Games");
        CategoryEntity categorySubClass = new CategoryEntity(){};

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(categorySubClass);

        Assertions.assertThrows(CategoryNotSavedException.class, () -> categoryService.createNewCategory(dto, id));

        verify(userService, times(1)).getOneUserByID(id);
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    public void createNewCategory_ThrowsUserNotFoundException(){
        CreateCategoryDto dto = new CreateCategoryDto("Games");
        when(userService.getOneUserByID(any(UUID.class))).thenThrow(new UserNotFoundException());


        Assertions.assertThrows(UserNotFoundException.class, () -> categoryService.createNewCategory(dto, id));

    }

    @Test
    public void createNewUser_ThrowsNullPointerException(){
        CreateCategoryDto dto = new CreateCategoryDto("Games");

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(null);
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () -> categoryService.createNewCategory(dto, id));
    }

    @Test
    public void createNewCategory_ThrowsIllegalArgumentException(){

        Assertions.assertThrows(IllegalArgumentException.class, () -> categoryService.createNewCategory(null, id));
    }

    @Test
    public void getAllCategories_ReturnCategoryList(){
        List<CategoryEntity> categoryList = Arrays.asList(new CategoryEntity("Games"), new CategoryEntity("Food"));

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.findAllByUserID(any(UUID.class))).thenReturn(categoryList);

        List<CategoryEntity> finalList = categoryService.getAllCategories(id);

        Assertions.assertEquals(finalList, categoryList);

        verify(userService, times(1)).getOneUserByID(id);
        verify(categoryRepository, times(1)).findAllByUserID(id);
    }

    @Test
    public void getAllCategories_ThrowsCategoriesNotFoundException(){

        List<CategoryEntity> listEmpty = new ArrayList<>();

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.findAllByUserID(any(UUID.class))).thenReturn(listEmpty);

        Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryService.getAllCategories(id));

        verify(userService, times(1)).getOneUserByID(id);
        verify(categoryRepository, times(1)).findAllByUserID(id);
    }

    @Test
    public void getAllCategories_ThrowsUserNotFoundException(){
        when(userService.getOneUserByID(any(UUID.class))).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class, () -> categoryService.getAllCategories(id));

        verify(userService, times(1)).getOneUserByID(id);
    }

    @Test
    public void getAllCategories_ThrowsNullPointerException(){

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.findAllByUserID(any(UUID.class))).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () -> categoryService.getAllCategories(id));

        verify(userService, times(1)).getOneUserByID(id);
    }

    @Test
    public void getOneCategoryByID_ReturnCategory(){

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(category));

        CategoryEntity finalCategory = categoryService.getOneCategoryByID(id, id);

        Assertions.assertNotNull(finalCategory);
        Assertions.assertEquals(finalCategory, category);

        verify(userService, times(1)).getOneUserByID(id);
        verify(categoryRepository, times(1)).findByUserEntityAndID(id, id);
    }

    @Test
    public void getOneCategoryByID_ThrowsCategoryNotFoundException(){

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryService.getOneCategoryByID(id, id));

        verify(userService, times(1)).getOneUserByID(id);
        verify(categoryRepository, times(1)).findByUserEntityAndID(id, id);
    }

    @Test
    public void getOneCategoryByID_ThrowsUserNotFoundException(){

        when(userService.getOneUserByID(any(UUID.class))).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class, () -> categoryService.getOneCategoryByID(id, id));

        verify(userService, times(1)).getOneUserByID(id);
    }

    @Test
    public void getOneCategoryByID_ThrowsNullPointerException(){

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () -> categoryService.getOneCategoryByID(id, id));
    }

    @Test
    public void deleteCategory_ReturnVoid(){

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(category));

        categoryService.deleteCategoryByID(id, id);

        verify(userService, times(1)).getOneUserByID(id);
        verify(categoryRepository, times(1)).findByUserEntityAndID(id, id);
        verify(categoryRepository, times(1)).delete(category);

    }

    @Test
    public void deleteCategory_ThrowCategoryNotFoundException(){

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategoryByID(id, id));

        verify(userService, times(1)).getOneUserByID(id);
        verify(categoryRepository, times(1)).findByUserEntityAndID(id, id);

    }

    @Test
    public void deleteCategory_ThrowsUserNotFoundException(){

        when(userService.getOneUserByID(any(UUID.class))).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class, () -> categoryService.deleteCategoryByID(id, id));

        verify(userService, times(1)).getOneUserByID(id);
    }

    @Test
    public void deleteCategory_ThrowsNullPointerException(){

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () -> categoryService.deleteCategoryByID(id, id));

        verify(userService, times(1)).getOneUserByID(id);
        verify(categoryRepository, times(1)).findByUserEntityAndID(id, id);

    }

    @Test
    public void updateCategory_ReturnCategory(){
        CategoryUpdateDto dto = new CategoryUpdateDto("Food");

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(new CategoryEntity("Food"));

        CategoryEntity finalCategory = categoryService.updateCategoryByID(id, id, dto);

        Assertions.assertNotNull(finalCategory);
        Assertions.assertEquals("Food", finalCategory.getName());

        verify(userService, times(1)).getOneUserByID(id);
        verify(categoryRepository, times(1)).findByUserEntityAndID(id, id);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void updateCategory_ThrowsCategoryNotSavedException(){
        CategoryUpdateDto dto = new CategoryUpdateDto("Food");

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.findByUserEntityAndID(any(UUID.class),any(UUID.class))).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(new CategoryEntity(){});

        Assertions.assertThrows(CategoryNotSavedException.class, () -> categoryService.updateCategoryByID(id, id, dto));

        verify(userService, times(1)).getOneUserByID(id);
        verify(categoryRepository, times(1)).findByUserEntityAndID(id, id);
        verify(categoryRepository, times(1)).save(category);

    }

    @Test
    public void updateCategory_ThrowsCategoryNotFoundException(){
        CategoryUpdateDto dto = new CategoryUpdateDto("Food");

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategoryByID(id, id, dto));

        verify(userService, times(1)).getOneUserByID(id);
        verify(categoryRepository, times(1)).findByUserEntityAndID(id, id);
    }

    @Test
    public void updateCategory_ThrowsUserNotFoundException(){
        CategoryUpdateDto dto = new CategoryUpdateDto("Food");

        when(userService.getOneUserByID(any(UUID.class))).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class, () -> categoryService.updateCategoryByID(id, id, dto));

        verify(userService, times(1)).getOneUserByID(id);
    }

    @Test
    public void updateCategory_ThrowsNullPointerException(){

        Assertions.assertThrows(NullPointerException.class, () -> categoryService.updateCategoryByID(id, id, null));
    }
}
