package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Controller.UserController;
import com.project.RastreadorDeFinancas.Dtos.Create.CreateUserDto;
import com.project.RastreadorDeFinancas.Dtos.LoginRequestDto;
import com.project.RastreadorDeFinancas.Dtos.Response.UserResponseDto;
import com.project.RastreadorDeFinancas.Dtos.Update.UserUpdateDto;
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

    public UserResponseDto createNewUser(@RequestBody @Validated CreateUserDto createUserDto) throws UserNotSavedException{
        UserEntity newUser = new UserEntity();

        BeanUtils.copyProperties(createUserDto, newUser);

        this.saveUser(newUser);

        return new UserResponseDto(newUser.getName(), newUser.getEmail(), newUser.getID());
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

    public boolean deleteUserByID(UUID idUser) throws UserNotFoundException {
        UserEntity user = this.getOneUserByID(idUser);

        this.userRepository.delete(user);

        return true;

    }

    public UserResponseDto updateUserByID(UUID idUser, @RequestBody @Validated UserUpdateDto userUpdateDto) throws UserNotFoundException, UserNotSavedException {
        UserEntity user = this.getOneUserByID(idUser);
        UserEntity userAux = new UserEntity();

        BeanUtils.copyProperties(userUpdateDto, userAux);

        if(!(userAux.getName().isEmpty())){
            user.setName(userAux.getName());
        }

        if(!(userAux.getEmail().isEmpty())){
            user.setEmail(userAux.getEmail());
        }

        this.saveUser(user);

        return new UserResponseDto(user.getName(), user.getEmail(), user.getID());
    }

    public boolean saveUser(UserEntity user){
        if(this.userRepository.save(user).getClass() == UserEntity.class){
            return true;
        }
        throw new UserNotSavedException();
    }

    public UserResponseDto verifyLogin(@RequestBody LoginRequestDto loginRequestDto){
        Optional<UserEntity> possibleUser = this.userRepository.getUserEntityByEmailAndPassword(loginRequestDto.email(), loginRequestDto.password());

        if(possibleUser.isPresent()){

            UserEntity user = possibleUser.get();

            return new UserResponseDto(user.getName(), user.getEmail(), user.getID());

        }
        else{
            throw new UserNotFoundException();
        }
    }
}
