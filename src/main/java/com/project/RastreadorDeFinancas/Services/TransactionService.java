package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Controller.TransactionController;
import com.project.RastreadorDeFinancas.Dtos.CreateTransactionDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Entities.TransactionEntity;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.*;
import com.project.RastreadorDeFinancas.Repository.CategoryRepository;
import com.project.RastreadorDeFinancas.Repository.TransactionRepository;
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
public class TransactionService {

    @Setter
    @Getter
    private UserRepository userRepository;

    @Setter
    @Getter
    private CategoryRepository categoryRepository;

    @Setter
    @Getter
    private TransactionRepository transactionRepository;

    @Setter
    @Getter
    private UserService userService;

    @Setter
    @Getter
    private CategoryService categoryService;


    public TransactionService(UserRepository userRepository, CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.userService = new UserService(userRepository);
        this.categoryService = new CategoryService(userRepository, categoryRepository);
    }

    public TransactionEntity createNewTransaction(@RequestBody @Validated CreateTransactionDto createTransactionDto, UUID idUser, UUID idCategory) {
        TransactionEntity transaction = new TransactionEntity();
        UserEntity user = userService.getOneUser(idUser);
        CategoryEntity category = categoryService.getOneCategory(idUser, idCategory);

        BeanUtils.copyProperties(createTransactionDto, transaction);

        transaction.setUserEntity(user);
        transaction.setCategoryEntity(category);

        this.transactionRepository.save(transaction);

        return transaction;
    }

    public TransactionEntity getOneTransaction(UUID idUser, UUID idTransaction) throws TransactionNotFoundException, UserNotFoundException {
        UserEntity user = userService.getOneUser(idUser);

        Optional<TransactionEntity> transaction = this.transactionRepository.findByUserEntityAndID(user, idTransaction);

        if(transaction.isPresent()){
            return transaction.get();
        }
        else{
            throw new TransactionNotFoundException();
        }

    }

    public List<TransactionEntity> getAllTransactions(UUID idUser) throws UserNotFoundException {
        UserEntity user = this.userService.getOneUser(idUser);

        List<TransactionEntity> transactionList = this.transactionRepository.findAllByUserEntity(user);

        for(TransactionEntity transaction: transactionList){
            transaction.add(linkTo(methodOn(TransactionController.class).getOneTransactionById(idUser, transaction.getID())).withSelfRel());
        }

        return transactionList;
    }

    public void deleteTransaction(UUID idUser, UUID idTransaction) throws UserNotFoundException, TransactionNotFoundException{
        TransactionEntity transaction = this.getOneTransaction(idUser, idTransaction);


        this.transactionRepository.deleteById(idTransaction);
    }

    public TransactionEntity updateTransaction(UUID idUser, UUID idTransaction, @RequestBody @Validated CreateTransactionDto createTransactionDto) throws UserNotFoundException, TransactionNotFoundException{
        TransactionEntity transaction = this.getOneTransaction(idUser, idTransaction);

        TransactionEntity transactionAux = new TransactionEntity();

        BeanUtils.copyProperties(createTransactionDto, transactionAux);

        transaction.setValue(transactionAux.getValue());
        transaction.setDate(transactionAux.getDate());
        transaction.setDescription(transactionAux.getDescription());
        transaction.setType(transactionAux.getType());

        this.transactionRepository.save(transaction);

        return transaction;
    }
}