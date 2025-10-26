package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Controller.UserController;
import com.project.RastreadorDeFinancas.Dtos.CreateUserDto;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
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

    public UserEntity createNewUser(@RequestBody @Validated CreateUserDto createUserDto) {
        UserEntity newUser = new UserEntity();

        BeanUtils.copyProperties(createUserDto, newUser);

        this.userRepository.save(newUser);

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

    public UserEntity getOneUser(UUID idUser) throws UserNotFoundException{
        Optional<UserEntity> possibleUser = this.userRepository.findById(idUser);

        if(possibleUser.isPresent()){
            return possibleUser.get();
        }
        else{
            throw new UserNotFoundException("There aren`t any user with this ID");
        }
    }

    public void deleteUser(UUID idUser) throws UserNotFoundException {
        Optional<UserEntity> user = this.userRepository.findById(idUser);

        if (user.isPresent()) {
            this.userRepository.deleteById(idUser);
        }

        throw new UserNotFoundException("There aren`t any users with this ID");
    }

    public UserEntity editUser(UUID idUser, @RequestBody @Validated CreateUserDto createUserDto) throws UserNotFoundException {
        Optional<UserEntity> possibleUser = this.userRepository.findById(idUser);

        if (possibleUser.isPresent()) {
            UserEntity userAux = new UserEntity();

            BeanUtils.copyProperties(createUserDto, userAux);

            UserEntity user = possibleUser.get();

            user.setName(userAux.getName());
            user.setEmail(userAux.getEmail());
            user.setPassword(userAux.getPassword());
            user.setCPF(userAux.getCPF());

            this.userRepository.save(user);

            return user;
        }

        throw new UserNotFoundException("There isn`t any user with this ID");
    }
}
