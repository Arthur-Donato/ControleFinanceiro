package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Controller.UserController;
import com.project.RastreadorDeFinancas.Dtos.UserRecordDto;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
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
public class UserService {

    @Setter
    @Getter
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserEntity createNewUser(@RequestBody @Validated UserRecordDto userRecordDto){
        UserEntity newUser  = new UserEntity();

        BeanUtils.copyProperties(userRecordDto, newUser);

        this.userRepository.save(newUser);

        return newUser;
    }

    public List<UserEntity> getAllUsers(){
        List<UserEntity> usersList = this.userRepository.findAll();

        if(!usersList.isEmpty()){
            for(UserEntity user : usersList) {
                user.add(linkTo(methodOn(UserController.class).getUserById(user.getID())).withSelfRel());
            }
        }

        return usersList;
    }

    public Optional<UserEntity> getOneUserById(UUID idUser){
        return userRepository.findById(idUser);
    }


    public void deleteUser(UUID idUser) throws UserNotFoundException {
        Optional<UserEntity> user = this.userRepository.findById(idUser);

        if(user.isPresent()){
            this.userRepository.deleteById(idUser);
        }

        throw new UserNotFoundException("There aren`t any users with this ID");
    }

    public UserEntity editUser(UUID idUser, @RequestBody @Validated UserRecordDto userRecordDto) throws UserNotFoundException{
        Optional<UserEntity> possibleUser = this.userRepository.findById(idUser);

        if(possibleUser.isPresent()){
            UserEntity userAux = new UserEntity();

            BeanUtils.copyProperties(userRecordDto, userAux);

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
