package com.project.RastreadorDeFinancas.Controller;

import com.project.RastreadorDeFinancas.Dtos.CreateTransactionDto;
import com.project.RastreadorDeFinancas.Dtos.TransactionResponseDto;
import com.project.RastreadorDeFinancas.Entities.TransactionEntity;
import com.project.RastreadorDeFinancas.Exceptions.TransactionNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Repository.CategoryRepository;
import com.project.RastreadorDeFinancas.Repository.TransactionRepository;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import com.project.RastreadorDeFinancas.Services.TransactionService;
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
        TransactionEntity newTransaction = transactionService.createNewTransaction(createTransactionDto, idUser, idCategory);

        TransactionResponseDto transactionResponseDto = new TransactionResponseDto(newTransaction);

        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDto);
    }

    @GetMapping(path = "/{idTransaction}")
    public ResponseEntity<TransactionResponseDto> getOneTransactionById(@PathVariable (value = "idUser") UUID idUser, @PathVariable (value = "idTransaction") UUID idTransaction){
        try{
            TransactionEntity transaction = this.transactionService.getOneTransaction(idUser, idTransaction);

            TransactionResponseDto transactionResponseDto = new TransactionResponseDto(transaction);

            return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDto);
        }
        catch(TransactionNotFoundException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions(@PathVariable (value = "idUser") UUID idUser){
        try{
            List<TransactionResponseDto> transactionsList = transactionService.getAllTransactions(idUser).stream().map(TransactionResponseDto::new).toList();

            return ResponseEntity.status(HttpStatus.OK).body(transactionsList);
        }catch(UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(path = "/{idTransaction}")
    public ResponseEntity<TransactionResponseDto> deleteTransaction(@PathVariable(value = "idUser") UUID idUser, @PathVariable(value = "idTransaction") UUID idTransaction){
        try{
            this.transactionService.deleteTransaction(idUser, idTransaction);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(UserNotFoundException | TransactionNotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(path = "/{idTransaction}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(@PathVariable(value = "idUser") UUID idUser, @PathVariable(value = "idTransaction") UUID idTransaction, @RequestBody @Validated CreateTransactionDto createTransactionDto){
        try{
            TransactionEntity newTransaction = this.transactionService.updateTransaction(idUser, idTransaction, createTransactionDto);

            TransactionResponseDto transactionResponseDto = new TransactionResponseDto(newTransaction);

            return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDto);
        }catch(UserNotFoundException | TransactionNotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
