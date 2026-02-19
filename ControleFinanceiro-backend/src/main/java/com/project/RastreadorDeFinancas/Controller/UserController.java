package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.Create.CreateUserDto;
import com.project.RastreadorDeFinancas.Dtos.Response.UserResponseDto;
import com.project.RastreadorDeFinancas.Dtos.Update.UserUpdateDto;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import com.project.RastreadorDeFinancas.Services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class    UserController {
    
    @Setter
    @Getter
    private UserService userService;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userService = new UserService(userRepository);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> postUser(@RequestBody @Validated CreateUserDto createUserDto){
        UserEntity newUser = userService.createNewUser(createUserDto);

        UserResponseDto userResponseDto = new UserResponseDto(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponseDto> getOneUserById(@PathVariable(value = "id") UUID id){
        try{
            UserEntity user = this.userService.getOneUserByID(id);

            UserResponseDto userResponseDto = new UserResponseDto(user);

            return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
        } catch(UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        try{
            List<UserResponseDto> userList = this.userService.getAllUsers().stream().map(UserResponseDto::new).toList();

            return ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch(UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable(value = "id")UUID id){
        try{
            userService.deleteUserByID(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        }catch(UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(path = "/put/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable (value = "id") UUID id, @RequestBody @Validated UserUpdateDto userUpdateDto){
        try{
            UserEntity user = userService.updateUserByID(id, userUpdateDto);

            UserResponseDto userResponseDto = new UserResponseDto(user);

            return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
        } catch(UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
