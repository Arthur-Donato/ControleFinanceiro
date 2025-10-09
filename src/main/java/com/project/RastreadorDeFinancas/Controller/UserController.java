package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.UserRecordDto;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
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

    @PostMapping
    public ResponseEntity<UserEntity> postUser(@RequestBody @Validated UserRecordDto userRecordDto){
        var newUser = new UserEntity();
        BeanUtils.copyProperties(userRecordDto, newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(newUser));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "id")UUID id){
        //VERIFICAR SE O USUARIO EXISTE E EXCLUIR ELE DO BANCO DE DADOS

        Optional<UserEntity> userDelete = this.userRepository.findById(id);

        if(userDelete.isPresent()){
            //EXISTE UM USUARIO PARA SER EXCLUIDO DO BANDO DE DADOS
            this.userRepository.deleteById(id);

            return ResponseEntity.status(HttpStatus.OK).body("O usuário foi deletado com sucesso");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum usuário com esse id");
    }

    @PutMapping(path = "/put/{id}")
    public ResponseEntity<Object> editUser(@PathVariable (value = "id") UUID id, @RequestBody @Validated UserRecordDto userRecordDto){
        Optional<UserEntity> userFind = this.userRepository.findById(id);

        if(userFind.isPresent()){
            //SUBSTITUIR AS INFORMACOES DE UM USUARIO JA EXISTENTE E NAO CRIAR OUTRO USUARIO
            //ESSE RACIOCINIO NAO POSSO UTILIZAR
            UserEntity userEdit = userFind.get();

            userEdit.setCPF(userRecordDto.CPF());
            userEdit.setName(userRecordDto.name());
            userEdit.setEmail(userRecordDto.email());
            userEdit.setPassword(userRecordDto.password());

            return ResponseEntity.status(HttpStatus.OK).body(this.userRepository.save(userEdit));

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O usuário em questão não foi encontrado!");

    }
}
