package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Repository.CategoryRepository;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreateNewCategoryService {

    //@Autowired
    @Setter
    @Getter
    private UserRepository userRepository;

    //@Autowired
    @Setter
    @Getter
    private CategoryRepository categoryRepository;

    public CreateNewCategoryService(UserRepository userRepository, CategoryRepository categoryRepository){
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;

    }

    public CreateNewCategoryService(){

    }

    @Transactional
    public CategoryEntity associateCategoryUser(String categoryName, List<UserEntity> listUsers){


        if(!listUsers.isEmpty()){
            CategoryEntity newCategory = new CategoryEntity(categoryName);

            UserEntity user = listUsers.getFirst();
            newCategory.setUserEntity(user);

            //this.categoryRepository.save(newCategory);

            return newCategory;

        }

        System.out.println("Não foi encontrado nenhum usuário com esse ID");

        return null;
    }

}
