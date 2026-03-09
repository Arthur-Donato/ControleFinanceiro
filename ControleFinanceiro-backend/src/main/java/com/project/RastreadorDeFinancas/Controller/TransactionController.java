package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.Create.CreateTransactionDto;
import com.project.RastreadorDeFinancas.Dtos.Response.TransactionResponseDto;
import com.project.RastreadorDeFinancas.Dtos.Update.TransactionUpdateDto;
import com.project.RastreadorDeFinancas.Exceptions.TransactionNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Repository.CategoryRepository;
import com.project.RastreadorDeFinancas.Repository.TransactionRepository;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import com.project.RastreadorDeFinancas.Services.TransactionService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/transaction/{idUser}")
public class TransactionController {

    @Setter
    @Getter
    private TransactionService transactionService;

    @Autowired
    public TransactionController(UserRepository userRepository, CategoryRepository categoryRepository, TransactionRepository transactionRepository){
        this.transactionService = new TransactionService(userRepository, categoryRepository, transactionRepository);
    }

    @PostMapping(path = "/{idCategory}")
    public ResponseEntity<TransactionResponseDto> postTransaction(@RequestBody @Validated CreateTransactionDto createTransactionDto, @PathVariable (value = "idUser") UUID idUser, @PathVariable (value = "idCategory") UUID idCategory){
        TransactionResponseDto newTransaction = transactionService.createNewTransaction(createTransactionDto, idUser, idCategory);

        return ResponseEntity.status(HttpStatus.OK).body(newTransaction);
    }

    @GetMapping(path = "/{idTransaction}")
    public ResponseEntity<TransactionResponseDto> getOneTransactionById(@PathVariable (value = "idUser") UUID idUser, @PathVariable (value = "idTransaction") UUID idTransaction){
        try{
            TransactionResponseDto transaction = this.transactionService.getOneTransactionResponseByID(idUser, idTransaction);

            return ResponseEntity.status(HttpStatus.OK).body(transaction);
        }
        catch(TransactionNotFoundException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<TransactionResponseDto>>> getAllTransactions(@PathVariable (value = "idUser") UUID idUser){
        try{
            List<EntityModel<TransactionResponseDto>> transactionsList = transactionService.getAllTransactions(idUser);

            return ResponseEntity.status(HttpStatus.OK).body(transactionsList);
        }catch(UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(path = "/{idTransaction}")
    public ResponseEntity<TransactionResponseDto> deleteTransaction(@PathVariable(value = "idUser") UUID idUser, @PathVariable(value = "idTransaction") UUID idTransaction){
        try{
            this.transactionService.deleteTransactionByID(idUser, idTransaction);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(UserNotFoundException | TransactionNotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(path = "/{idTransaction}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(@PathVariable(value = "idUser") UUID idUser, @PathVariable(value = "idTransaction") UUID idTransaction, @RequestBody @Validated TransactionUpdateDto transactionUpdateDto){
        try{
            TransactionResponseDto transactionResponseDto = this.transactionService.updateTransactionByID(idUser, idTransaction, transactionUpdateDto);

            return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDto);
        }catch(UserNotFoundException | TransactionNotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
