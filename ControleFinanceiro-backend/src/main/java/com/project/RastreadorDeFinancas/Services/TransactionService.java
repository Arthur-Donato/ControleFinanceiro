package com.project.RastreadorDeFinancas.Services;

import com.project.RastreadorDeFinancas.Controller.TransactionController;
import com.project.RastreadorDeFinancas.Dtos.Transaction.CreateTransactionDto;
import com.project.RastreadorDeFinancas.Dtos.Transaction.TransactionResponseDto;
import com.project.RastreadorDeFinancas.Dtos.User.UserResponseDto;
import com.project.RastreadorDeFinancas.Dtos.Transaction.TransactionUpdateDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Entities.TransactionEntity;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.*;
import com.project.RastreadorDeFinancas.Repository.TransactionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class TransactionService {

    @Getter
    private final TransactionRepository transactionRepository;

    @Getter
    private final UserService userService;

    @Getter
    private final CategoryService categoryService;

    public TransactionResponseDto createNewTransaction(@RequestBody @Validated CreateTransactionDto createTransactionDto, UUID idUser, UUID idCategory) throws UserNotFoundException, CategoryNotFoundException{
        TransactionEntity newTransaction = new TransactionEntity();

        BeanUtils.copyProperties(createTransactionDto, newTransaction);

        this.attributeCategoryAndUserToNewTransaction(idUser, idCategory, newTransaction);

        this.saveTransaction(newTransaction);

        return new TransactionResponseDto(newTransaction);
    }

    public void attributeCategoryAndUserToNewTransaction(UUID idUser, UUID idCategory, TransactionEntity newTransaction) throws UserNotFoundException, CategoryNotFoundException{
        UserEntity user = this.userService.getOneUserByID(idUser);
        CategoryEntity category = this.categoryService.getOneCategoryByID(idUser, idCategory);

        newTransaction.setUserEntity(user);
        newTransaction.setCategoryEntity(category);
    }

    public List<EntityModel<TransactionResponseDto>> getAllTransactions(UUID idUser) throws UserNotFoundException {
        UserResponseDto user = this.returnUserResponseDto(idUser);

        List<TransactionEntity> transactionList = this.transactionRepository.findTransactionEntitiesByUserEntity_ID(user.idUser());

        if(transactionList.isEmpty()){
            throw new UserNotFoundException();
        }

        return transactionList.stream().map(transactionEntity -> {
            TransactionResponseDto transactionResponseDto = new TransactionResponseDto(transactionEntity);

            EntityModel<TransactionResponseDto> resource = EntityModel.of(transactionResponseDto);

            resource.add(linkTo(methodOn(TransactionController.class).getOneTransactionById(user.idUser(), transactionResponseDto.idTransaction())).withSelfRel());

            return resource;
        }).collect(Collectors.toList());

    }

    public TransactionEntity getTransactionEntityByID(UUID idUser, UUID idTransaction) throws UserNotFoundException{
        return getOneTransactionEntityByID(idUser, idTransaction);
    }

    protected TransactionEntity getOneTransactionEntityByID(UUID idUser, UUID idTransaction) throws UserNotFoundException {
        UserEntity user = userService.getOneUserByID(idUser);

        Optional<TransactionEntity> transaction = this.transactionRepository.findByUserEntityAndID(user.getID(), idTransaction);

        if(transaction.isPresent()){
            return transaction.get();
        }
        else{
            throw new TransactionNotFoundException();
        }

    }

    public TransactionResponseDto getOneTransactionResponseByID(UUID idUser, UUID idTransaction) throws UserNotFoundException{
        UserEntity user = userService.getOneUserByID(idUser);

        Optional<TransactionEntity> transactionOptional = this.transactionRepository.findByUserEntityAndID(user.getID(), idTransaction);

        if(transactionOptional.isPresent()){
            return new TransactionResponseDto(transactionOptional.get());
        }

        throw new TransactionNotFoundException();
    }

    public void deleteTransactionByID(UUID idUser, UUID idTransaction) throws UserNotFoundException, TransactionNotFoundException{
        TransactionEntity transaction = this.getOneTransactionEntityByID(idUser, idTransaction);


        this.transactionRepository.deleteById(idTransaction);
    }

    public TransactionResponseDto updateTransactionByID(UUID idUser, UUID idTransaction, @RequestBody @Validated TransactionUpdateDto transactionUpdateDto) throws UserNotFoundException, TransactionNotFoundException, TransactionNotSavedException{
        TransactionEntity transaction = this.getOneTransactionEntityByID(idUser, idTransaction);

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

        return new TransactionResponseDto(transaction);
    }

    public boolean saveTransaction(TransactionEntity transaction){
        if(this.transactionRepository.save(transaction).getClass() == TransactionEntity.class){
            return true;
        }

        throw new TransactionNotSavedException();
    }

    private UserResponseDto returnUserResponseDto(UUID idUser){
        UserEntity user = this.userService.getOneUserByID(idUser);

        return new UserResponseDto(user);
    }
}