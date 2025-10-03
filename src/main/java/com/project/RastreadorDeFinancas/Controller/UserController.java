package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "id") UUID id){
        Optional<UserEntity> userByID = this.userRepository.findById(id);

        if(userByID.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao foi encontrada");
        }
        else{
            userByID.get().add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
            return ResponseEntity.status(HttpStatus.OK).body(userByID.get());
        }
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        //TODO: Fazer uma consulta no banco de dados e retornar todos os usuarios
        List<UserEntity> userList = userRepository.findAll();

        if(!userList.isEmpty()){
            for(UserEntity user : userList){
                user.add(linkTo(methodOn(UserController.class).getUserById(user.getID())).withSelfRel());
            }
        }

        return ResponseEntity.ok(userList);
    }
}
