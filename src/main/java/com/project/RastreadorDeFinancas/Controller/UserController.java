package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.UserRecordDto;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import com.project.RastreadorDeFinancas.Services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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


    @GetMapping(path = "/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable(value = "id") UUID id){
        Optional<UserEntity> userEntity = userService.getOneUserById(id);

        if(userEntity.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(userEntity.get());
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        List<UserEntity> usersList = userService.getAllUsers();

        if(!usersList.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(usersList);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PostMapping
    public ResponseEntity<UserEntity> postUser(@RequestBody @Validated UserRecordDto userRecordDto){
        UserEntity newUser = userService.createNewUser(userRecordDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "id")UUID id){
        try{

            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        }catch(UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(path = "/put/{id}")
    public ResponseEntity<Object> editUser(@PathVariable (value = "id") UUID id, @RequestBody @Validated UserRecordDto userRecordDto){

        try{
            UserEntity user = userService.editUser(id, userRecordDto);

            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch(UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
