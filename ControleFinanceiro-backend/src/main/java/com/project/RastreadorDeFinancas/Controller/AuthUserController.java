package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.User.CreateUserDto;
import com.project.RastreadorDeFinancas.Dtos.LoginRequestDto;
import com.project.RastreadorDeFinancas.Dtos.User.UserResponseDto;
import com.project.RastreadorDeFinancas.Dtos.User.UserUpdateDto;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotSavedException;
import com.project.RastreadorDeFinancas.Services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthUserController {

    @Getter
    private final UserService userService;

    @PostMapping(path = "/login")
    private ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        try{
            UserResponseDto userDto = userService.verifyLogin(loginRequestDto);

            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        }
        catch(UserNotFoundException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(path = "/cadastro")
    public ResponseEntity<UserResponseDto> createNewUser(@RequestBody CreateUserDto createUserDto){
        try{
            UserResponseDto newUserDto = this.userService.createNewUser(createUserDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(newUserDto);

        }catch(UserNotSavedException e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable(value = "id") UUID userID, @RequestBody @Validated UserUpdateDto userUpdateDto){

        try{
            UserResponseDto userUpdatedDto = this.userService.updateUserByID(userID, userUpdateDto);

            return ResponseEntity.status(HttpStatus.OK).body(userUpdatedDto);

        }catch(Exception e){
            if(e.getClass() == UserNotFoundException.class){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

    }

}
