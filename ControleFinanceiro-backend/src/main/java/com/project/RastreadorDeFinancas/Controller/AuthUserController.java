package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.Create.CreateUserDto;
import com.project.RastreadorDeFinancas.Dtos.LoginRequestDto;
import com.project.RastreadorDeFinancas.Dtos.Update.UserUpdateDto;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotSavedException;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import com.project.RastreadorDeFinancas.Services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/auth")
@CrossOrigin(origins = "*")
public class AuthUserController {

    @Getter
    @Setter
    private UserService userService;

    @Autowired
    public AuthUserController(UserRepository userRepository){
        this.userService = new UserService(userRepository);
    }

    @PostMapping(path = "/login")
    private ResponseEntity<UserEntity> login(@RequestBody LoginRequestDto loginRequestDto){
        try{
            UserEntity user = userService.verifyLogin(loginRequestDto);

            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        catch(UserNotFoundException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(path = "/cadastro")
    public ResponseEntity<UserEntity> createNewUser(@RequestBody CreateUserDto createUserDto){
        try{
            UserEntity newUser = this.userService.createNewUser(createUserDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

        }catch(UserNotSavedException e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable(value = "id") UUID userID, @RequestBody @Validated UserUpdateDto userUpdateDto){


        try{
            UserEntity userUpdated = this.userService.updateUserByID(userID, userUpdateDto);

            return ResponseEntity.status(HttpStatus.OK).body(userUpdated);

        }catch(Exception e){
            if(e.getClass() == UserNotFoundException.class){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

    }

}
