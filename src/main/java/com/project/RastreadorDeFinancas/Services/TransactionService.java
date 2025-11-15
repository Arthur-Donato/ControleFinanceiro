package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Controller.TransactionController;
import com.project.RastreadorDeFinancas.Dtos.CreateTransactionDto;
import com.project.RastreadorDeFinancas.Dtos.TransactionUpdateDto;
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

    public TransactionEntity createNewTransaction(@RequestBody @Validated CreateTransactionDto createTransactionDto, UUID idUser, UUID idCategory) throws UserNotFoundException, CategoryNotFoundException{
        TransactionEntity newTransaction = new TransactionEntity();

        BeanUtils.copyProperties(createTransactionDto, newTransaction);

        this.attributeCategoryAndUserToNewTransaction(idUser, idCategory, newTransaction);

        this.saveTransaction(newTransaction);

        return newTransaction;
    }

    public void attributeCategoryAndUserToNewTransaction(UUID idUser, UUID idCategory, TransactionEntity newTransaction) throws UserNotFoundException, CategoryNotFoundException{
        UserEntity user = this.userService.getOneUserByID(idUser);
        CategoryEntity category = this.categoryService.getOneCategoryByID(idUser, idCategory);

        newTransaction.setUserEntity(user);
        newTransaction.setCategoryEntity(category);
    }

    public List<TransactionEntity> getAllTransactions(UUID idUser) throws UserNotFoundException {
        UserEntity user = this.userService.getOneUserByID(idUser);

        List<TransactionEntity> transactionList = this.transactionRepository.findAllByUserEntity(user);

        if(!transactionList.isEmpty()){
            for(TransactionEntity transaction: transactionList){
                transaction.add(linkTo(methodOn(TransactionController.class).getOneTransactionById(idUser, transaction.getID())).withSelfRel());
            }

            return transactionList;
        }

        throw new TransactionNotFoundException();

    }

    public TransactionEntity getOneTransactionByID(UUID idUser, UUID idTransaction) throws UserNotFoundException {
        UserEntity user = userService.getOneUserByID(idUser);

        Optional<TransactionEntity> transaction = this.transactionRepository.findByUserEntityAndID(user.getID(), idTransaction);

        if(transaction.isPresent()){
            return transaction.get();
        }
        else{
            throw new TransactionNotFoundException();
        }

    }

    public void deleteTransactionByID(UUID idUser, UUID idTransaction) throws UserNotFoundException, TransactionNotFoundException{
        TransactionEntity transaction = this.getOneTransactionByID(idUser, idTransaction);


        this.transactionRepository.deleteById(idTransaction);
    }

    public TransactionEntity updateTransactionByID(UUID idUser, UUID idTransaction, @RequestBody @Validated TransactionUpdateDto transactionUpdateDto) throws UserNotFoundException, TransactionNotFoundException, TransactionNotSavedException{
        TransactionEntity transaction = this.getOneTransactionByID(idUser, idTransaction);

        TransactionEntity transactionAux = new TransactionEntity();

        BeanUtils.copyProperties(transactionUpdateDto, transactionAux);

        if(!(transactionAux.getValue() == null)){
            transaction.setValue(transactionAux.getValue());
        }

        if(!(transactionAux.getType() == null)){
            transaction.setType(transactionAux.getType());
        }

        if(!(transactionAux.getDescription() == null)){
            transaction.setDescription(transactionAux.getDescription());
        }

        this.saveTransaction(transaction);

        return transaction;
    }

    public boolean saveTransaction(TransactionEntity transaction){
        if(this.transactionRepository.save(transaction).getClass() == TransactionEntity.class){
            return true;
        }

        throw new TransactionNotSavedException();
    }
}