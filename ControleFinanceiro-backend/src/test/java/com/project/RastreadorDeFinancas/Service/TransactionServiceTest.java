package com.project.RastreadorDeFinancas.Service;

import com.project.RastreadorDeFinancas.Dtos.CreateTransactionDto;
import com.project.RastreadorDeFinancas.Dtos.TransactionUpdateDto;
import com.project.RastreadorDeFinancas.Entities.CategoryEntity;
import com.project.RastreadorDeFinancas.Entities.TransactionEntity;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.CategoryNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.TransactionNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.TransactionNotSavedException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Repository.TransactionRepository;
import com.project.RastreadorDeFinancas.Services.CategoryService;
import com.project.RastreadorDeFinancas.Services.TransactionService;
import com.project.RastreadorDeFinancas.Services.UserService;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private UserService userService;

    private UUID idCategory;

    private UUID idUser;

    private UUID idTransaction;

    private TransactionEntity transaction;

    private Instant time;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        idCategory = UUID.randomUUID();
        idUser = UUID.randomUUID();
        idTransaction = UUID.randomUUID();
        time = Instant.now();
        transaction = new TransactionEntity("Despesa", 52.60, "", time);
        transaction.setID(idTransaction);
    }

    @Test
    public void saveTransaction_ReturnTrue(){

        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transaction);

        Assertions.assertTrue(transactionService.saveTransaction(transaction));

        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void saveTransaction_ThrowsTransactionNotSavedException(){
        TransactionEntity transactionSubClass = new TransactionEntity(){};

        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionSubClass);

        Assertions.assertThrows(TransactionNotSavedException.class, () -> transactionService.saveTransaction(transaction));

        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void saveTransaction_ThrowsNullPointerException(){

        Assertions.assertThrows(NullPointerException.class, () -> transactionService.saveTransaction(null));

        verify(transactionRepository, times(1)).save(null);
    }

    @Test
    public void attributeCategoryAndUserToNewTransaction_ReturnSomething(){

        when(userService.getOneUserByID(idUser)).thenReturn(new UserEntity("293847", "Arthur", "arthur@", "123"));
        when(categoryService.getOneCategoryByID(idUser, idCategory)).thenReturn(new CategoryEntity("Games"));

        transactionService.attributeCategoryAndUserToNewTransaction(idUser, idCategory, transaction);

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(categoryService, times(1)).getOneCategoryByID(idUser, idCategory);
    }

    @Test
    public void attributeCategoryAndUserToNewTransaction_ThrowsUserNotFoundException(){

        when(userService.getOneUserByID(any(UUID.class))).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class, () -> transactionService.attributeCategoryAndUserToNewTransaction(idUser, idCategory, transaction));

        verify(userService, times(1)).getOneUserByID(idUser);

    }

    @Test
    public void attributeCategoryAndUserToNewTransaction_ThrowsCategoryNotFoundException(){

        UserEntity user  = new UserEntity("1293874", "Arthur", "arthur@", "123");

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryService.getOneCategoryByID(any(UUID.class), any(UUID.class))).thenThrow(new CategoryNotFoundException());

        Assertions.assertThrows(CategoryNotFoundException.class, () -> transactionService.attributeCategoryAndUserToNewTransaction(idUser, idCategory, transaction));

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(categoryService, times(1)).getOneCategoryByID(idUser, idCategory);

    }

    @Test
    public void attributeCategoryAndUserToNewTransaction_ThrowsNullPointerException(){
        UserEntity user  = new UserEntity("1293874", "Arthur", "arthur@", "123");
        CategoryEntity category = new CategoryEntity("Games");

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryService.getOneCategoryByID(any(UUID.class), any(UUID.class))).thenReturn(category);


        Assertions.assertThrows(NullPointerException.class, () -> transactionService.attributeCategoryAndUserToNewTransaction(idUser, idCategory, null));

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(categoryService, times(1)).getOneCategoryByID(idUser, idCategory);
    }

    @Test
    public void createNewTransaction_ReturnNewTransaction(){

        UserEntity user  = new UserEntity("1293874", "Arthur", "arthur@", "123");
        CreateTransactionDto dto = new CreateTransactionDto("Despesa", 52.60, "", time);
        CategoryEntity category = new CategoryEntity("Games");
        transaction.setUserEntity(user);
        transaction.setCategoryEntity(category);


        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryService.getOneCategoryByID(any(UUID.class), any(UUID.class))).thenReturn(category);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transaction);

        TransactionEntity newTransaction = transactionService.createNewTransaction(dto, idUser, idCategory);
        newTransaction.setID(idTransaction);

        Assertions.assertNotNull(newTransaction);
        Assertions.assertEquals(transaction, newTransaction);

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(categoryService, times(1)).getOneCategoryByID(idUser, idCategory);
        verify(transactionRepository, times(1)).save(transaction);

    }

    @Test
    public void createNewTransaction_ThrowsUserNotFoundException(){

        CreateTransactionDto dto = new CreateTransactionDto("Despesa", 52.60, " ", time);

        when(userService.getOneUserByID(any(UUID.class))).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class, () -> transactionService.createNewTransaction(dto, idUser, idCategory));

        verify(userService, times(1)).getOneUserByID(idUser);
    }

    @Test
    public void createNewTransaction_ThrowsCategoryNotFoundException(){

        CreateTransactionDto dto = new CreateTransactionDto("Despesa", 59.60, " ", time);
        UserEntity user = new UserEntity("945687", "Arthur", "arthur@gmail.com", "123");

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(categoryService.getOneCategoryByID(any(UUID.class), any(UUID.class))).thenThrow(new CategoryNotFoundException());


        Assertions.assertThrows(CategoryNotFoundException.class, () -> transactionService.createNewTransaction(dto, idUser, idCategory));

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(categoryService, times(1)).getOneCategoryByID(idUser, idCategory);
    }

    @Test
    public void createNewTransaction_ThrowsNullPointerException(){
        CreateTransactionDto dto = new CreateTransactionDto("Despesa", 32.4, " ", time);

        Assertions.assertThrows(NullPointerException.class, () -> transactionService.createNewTransaction(dto, null, idCategory));

    }

    @Test
    public void createNewTransaction_ThrowsIllegalArgumentException(){

        Assertions.assertThrows(IllegalArgumentException.class, () -> transactionService.createNewTransaction(null, idUser, idCategory));

    }

    @Test
    public void getAllTransactions_ReturnTransactionsList(){
        UserEntity user = new UserEntity("12314", "Arthru", "asda", "123");
        TransactionEntity secondTransaction = new TransactionEntity("Salario", 400.00, " ", time);

        secondTransaction.setID(idTransaction);

        List<TransactionEntity> transactionsList = Arrays.asList(transaction, secondTransaction);

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(transactionRepository.findAllByUserEntity(any(UserEntity.class))).thenReturn(transactionsList);

        List<TransactionEntity> finalList = transactionService.getAllTransactions(idUser);

        Assertions.assertEquals(transactionsList, finalList);

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(transactionRepository, times(1)).findAllByUserEntity(user);

    }

    @Test
    public void getAllTransactions_ThrowsUserNotFoundException(){

        when(userService.getOneUserByID(any(UUID.class))).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class, () -> transactionService.getAllTransactions(idUser));

        verify(userService, times(1)).getOneUserByID(idUser);
    }

    @Test
    public void getAllTransactions_ThrowsTransactionNotFoundException(){
        UserEntity user = new UserEntity("123124", "Arthur", "a34", "12314");

        List<TransactionEntity> emptyList = new ArrayList<>();


        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(transactionRepository.findAllByUserEntity(any(UserEntity.class))).thenReturn(emptyList);


        Assertions.assertThrows(TransactionNotFoundException.class, () -> transactionService.getAllTransactions(idUser));

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(transactionRepository, times(1)).findAllByUserEntity(user);

    }

    @Test
    public void getOneTransaction_ReturnTransaction(){

        UserEntity user = new UserEntity("12342345", "A", "a", "123");
        user.setID(idUser);

        Optional<TransactionEntity> opt = Optional.of(transaction);

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(transactionRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(opt);

        TransactionEntity transactionResponse = transactionService.getOneTransactionByID(idUser, idTransaction);
        transactionResponse.setID(idTransaction);

        Assertions.assertEquals(transaction, transactionResponse);

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(transactionRepository, times(1)).findByUserEntityAndID(idUser, idTransaction);
    };

    @Test
    public void getOneTransaction_ThrowsUserNotFoundException(){
        when(userService.getOneUserByID(any(UUID.class))).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class, () -> transactionService.getOneTransactionByID(idUser, idTransaction));

        verify(userService, times(1)).getOneUserByID(idUser);
    }

    @Test
    public void getOneTransaction_ThrowsTransactionNotFoundException(){
        UserEntity user = new UserEntity("12414", "Arhtur", "arthur@gmail.com", "124");
        user.setID(idUser);

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(transactionRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(TransactionNotFoundException.class, () -> transactionService.getOneTransactionByID(idUser, idTransaction));

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(transactionRepository, times(1)).findByUserEntityAndID(idUser, idTransaction);
    }

    @Test
    public void getOneTransaction_ThrowsNullPointerException(){
        UserEntity user = new UserEntity("12414", "Arhtur", "arthur@gmail.com", "124");
        user.setID(idUser);

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(transactionRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () -> transactionService.getOneTransactionByID(idUser, idTransaction));
    }

    @Test
    public void deleteTransaction_ReturnNothing(){
        UserEntity user = new UserEntity("12414", "Arhtur", "arthur@gmail.com", "124");
        user.setID(idUser);

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(transactionRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(transaction));

        //No need mock the delete method because returns void
        transactionService.deleteTransactionByID(idUser, idTransaction);

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(transactionRepository, times(1)).findByUserEntityAndID(idUser, idTransaction);
        verify(transactionRepository, times(1)).deleteById(idTransaction);
    }

    @Test
    public void deleteTransaction_ThrowsUserNotFoundException(){

        when(userService.getOneUserByID(any(UUID.class))).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class, () -> transactionService.deleteTransactionByID(idUser, idTransaction));

        verify(userService, times(1)).getOneUserByID(idUser);

    }

    @Test
    public void deleteTransaction_ThrowsTransactionNotFoundException(){
        UserEntity user = new UserEntity("12414", "Arhtur", "arthur@gmail.com", "124");
        user.setID(idUser);

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(transactionRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(TransactionNotFoundException.class, () -> transactionService.deleteTransactionByID(idUser, idTransaction));

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(transactionRepository, times(1)).findByUserEntityAndID(idUser, idTransaction);

    }

    @Test
    public void deleteTransaction_ThrowsNullPointerException(){
        UserEntity user = new UserEntity("12414", "Arhtur", "arthur@gmail.com", "124");
        user.setID(idUser);

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(transactionRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () -> transactionService.deleteTransactionByID(idUser, idTransaction));

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(transactionRepository, times(1)).findByUserEntityAndID(idUser, idTransaction);
    }

    @Test
    public void updateTransaction_ReturnTransaction(){
        TransactionUpdateDto dto = new TransactionUpdateDto("Salario", 400.00, "faculdade");

        UserEntity user = new UserEntity("12414", "Arhtur", "arthur@gmail.com", "124");
        user.setID(idUser);

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(transactionRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transaction);

        TransactionEntity transactionEdited = transactionService.updateTransactionByID(idUser, idTransaction, dto);

        Assertions.assertNotNull(transactionEdited);
        Assertions.assertEquals("Salario", transactionEdited.getType());
        Assertions.assertEquals(400.00, transactionEdited.getValue());
        Assertions.assertEquals("faculdade", transactionEdited.getDescription());

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(transactionRepository, times(1)).findByUserEntityAndID(idUser, idTransaction);
        verify(transactionRepository, times(1)).save(transaction);

    }

    @Test
    public void updateTransaction_ThrowsUserNotFoundException(){
        TransactionUpdateDto dto = new TransactionUpdateDto("Salario", 400.00, "faculdade");

        when(userService.getOneUserByID(any(UUID.class))).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class, () -> transactionService.updateTransactionByID(idUser, idTransaction, dto));

        verify(userService, times(1)).getOneUserByID(idUser);
    }

    @Test
    public void updateTransaction_ThrowsTransactionNotFoundException(){
        TransactionUpdateDto dto = new TransactionUpdateDto("Salario", 400.00, "faculdade");

        UserEntity user = new UserEntity("12414", "Arhtur", "arthur@gmail.com", "124");
        user.setID(idUser);

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(transactionRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(TransactionNotFoundException.class, () -> transactionService.updateTransactionByID(idUser, idTransaction, dto));

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(transactionRepository, times(1)).findByUserEntityAndID(idUser, idTransaction);
    }

    @Test
    public void updateTransaction_ThrowsTransactionNotSavedException(){
        TransactionUpdateDto dto = new TransactionUpdateDto("Salario", 400.00, "faculdade");

        UserEntity user = new UserEntity("12414", "Arhtur", "arthur@gmail.com", "124");
        user.setID(idUser);

        when(userService.getOneUserByID(any(UUID.class))).thenReturn(user);
        when(transactionRepository.findByUserEntityAndID(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(new TransactionEntity(){});

        Assertions.assertThrows(TransactionNotSavedException.class, () -> transactionService.updateTransactionByID(idUser, idTransaction, dto));

        verify(userService, times(1)).getOneUserByID(idUser);
        verify(transactionRepository, times(1)).findByUserEntityAndID(idUser, idTransaction);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void updateTransaction_ThrowsNullPointerException(){

        Assertions.assertThrows(NullPointerException.class, () -> transactionService.updateTransactionByID(idUser, idTransaction, null));

    }
}
