package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Controller.UserController;
import com.project.RastreadorDeFinancas.Dtos.CreateUserDto;
import com.project.RastreadorDeFinancas.Dtos.UserUpdateDto;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotSavedException;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService {

    @Setter
    @Getter
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createNewUser(@RequestBody @Validated CreateUserDto createUserDto) throws UserNotSavedException{
        UserEntity newUser = new UserEntity();

        BeanUtils.copyProperties(createUserDto, newUser);

        this.saveUser(newUser);

        return newUser;
    }

    public List<UserEntity> getAllUsers() {
        List<UserEntity> userList = this.userRepository.findAll();

        if (!userList.isEmpty()) {
            for (UserEntity user : userList) {
                user.add(linkTo(methodOn(UserController.class).getOneUserById(user.getID())).withSelfRel());
            }

            return userList;
        } else {
            throw new UserNotFoundException();
        }
    }

    public UserEntity getOneUserByID(UUID idUser){
        Optional<UserEntity> possibleUser = this.userRepository.findById(idUser);

        if(possibleUser.isPresent()){
            return possibleUser.get();
        }
        else{
            throw new UserNotFoundException();
        }
    }

    public void deleteUserByID(UUID idUser) throws UserNotFoundException {
        UserEntity user = this.getOneUserByID(idUser);

        this.userRepository.delete(user);

    }

    public UserEntity updateUserByID(UUID idUser, @RequestBody @Validated UserUpdateDto userUpdateDto) throws UserNotFoundException, UserNotSavedException {
        UserEntity user = this.getOneUserByID(idUser);
        UserEntity userAux = new UserEntity();

        BeanUtils.copyProperties(userUpdateDto, userAux);

        if(!(userAux.getName() == null)){
            user.setName(userAux.getName());
        }

        if(!(userAux.getEmail() == null)){
            user.setEmail(userAux.getEmail());
        }

        this.saveUser(user);

        return user;
    }

    public void saveUser(UserEntity user){
        if(this.userRepository.save(user).getClass() == UserEntity.class){
            return;
        }
        throw new UserNotSavedException();
    }
}
