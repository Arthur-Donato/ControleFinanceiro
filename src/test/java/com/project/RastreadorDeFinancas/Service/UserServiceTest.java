package com.project.RastreadorDeFinancas.Service;


import com.project.RastreadorDeFinancas.Dtos.CreateUserDto;
import com.project.RastreadorDeFinancas.Dtos.UserUpdateDto;
import com.project.RastreadorDeFinancas.Entities.UserEntity;
import com.project.RastreadorDeFinancas.Exceptions.UserNotFoundException;
import com.project.RastreadorDeFinancas.Exceptions.UserNotSavedException;
import com.project.RastreadorDeFinancas.Repository.UserRepository;
import com.project.RastreadorDeFinancas.Services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveUserTest_ReturnTrue(){
        //ARRANGE

        UserEntity firstUser = new UserEntity();

        when(userRepository.save(any(UserEntity.class))).thenReturn(firstUser);

        //ACT
        boolean returnValue = userService.saveUser(firstUser);

        //ASSERTS
        Assertions.assertTrue(returnValue);

        verify(userRepository, times(1)).save(firstUser);

    }

    @Test
    public void saveUserTest_ReturnUserNotSavedException(){
        //ARRANGE
        UserEntity user = new UserEntity();

        UserEntity userSubClass = new UserEntity(){

        };

        when(userRepository.save(any(UserEntity.class))).thenReturn(userSubClass);

        //ASSERTIONS + ACT

        Assertions.assertThrows(UserNotSavedException.class, () -> userService.saveUser(user));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void saveUserTest_ThrowsExceptionOnNullReturns(){
        //ARRANGE
        UserEntity user = new UserEntity();

        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        //ASSERTIONS + ACT

        Assertions.assertThrows(NullPointerException.class, () -> userService.saveUser(user));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void createNewUserTest_ReturnUser(){
        //ARRANGE
        CreateUserDto createUserDto = new CreateUserDto("0971273984", "Arthur Barbosa Donato", "arthur@gmail.com", "123");

        UserEntity firstUser = new UserEntity("0971273984", "Arthur Barbosa Donato", "arthur@gmail.com", "123");

        when(userRepository.save(any(UserEntity.class))).thenReturn(firstUser);

        //ACT

        UserEntity user = userService.createNewUser(createUserDto);

        //ASSERTIONS

        Assertions.assertNotNull(user);
        Assertions.assertEquals("Arthur Barbosa Donato", user.getName());
        Assertions.assertEquals("arthur@gmail.com", user.getEmail());
        Assertions.assertEquals("0971273984", user.getCPF());

        verify(userRepository, times(1)).save(user);

    }

    @Test
    public void createNewUserTest_ReturnUserNotSavedException(){
        //ARRANGE
        CreateUserDto createUserDto = new CreateUserDto("0971273984", "Arthur Barbosa Donato", "arthur@gmail.com", "123");

        UserEntity user = new UserEntity("0971273984", "Arthur Barbosa Donato", "arthur@gmail.com", "123");

        UserEntity userSubClass = new UserEntity(){
            //JUST A SUBCLASS TO THROW THE EXCEPTION
        };

        when(userRepository.save(any(UserEntity.class))).thenReturn(userSubClass);

        //ASSERTIONS + ACT

        Assertions.assertThrows(UserNotSavedException.class, () -> userService.createNewUser(createUserDto));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void createNewUserTest_ThrowsExceptionOnNullReturn(){
        CreateUserDto createUserDto = new CreateUserDto("0971273984", "Arthur Barbosa Donato", "arthur@gmail.com", "123");

        when(userRepository.save(any(UserEntity.class))).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () -> userService.createNewUser(createUserDto));

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void getOneUserByIDTest_ReturnUser(){
        //ARRANGE
        Optional<UserEntity> userToReturn = Optional.of(new UserEntity("0971273984", "Arthur Barbosa Donato", "arthur@gmail.com", "123"));
        UserEntity userExpected = new UserEntity("0971273984", "Arthur Barbosa Donato", "arthur@gmail.com", "123");

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(userToReturn);
        //ACT

        UserEntity user = userService.getOneUserByID(id);

        //ASSERTIONS

        Assertions.assertNotNull(user);
        Assertions.assertEquals(userExpected.getName(), user.getName());
        Assertions.assertEquals(userExpected.getCPF(), user.getCPF());
        Assertions.assertEquals(userExpected.getEmail(), user.getEmail());
        Assertions.assertEquals(userExpected.getPassword(), user.getPassword());

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void getOneUserByIDTest_ReturnUserNotFoundException(){
        //ARRANGE
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        //ASSERTIONS + ACT

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getOneUserByID(id));

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void deleteUserByIdTest_ReturnTrue(){
        //ARRANGE
        UUID id = UUID.randomUUID();
        Optional<UserEntity> user = Optional.of(new UserEntity());


        when(userRepository.findById(id)).thenReturn(user);

        //ACT

        boolean result = userService.deleteUserByID(id);

        //ASSERTIONS

        Assertions.assertTrue(result);

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void deleteUserByIDTest_ThrowsUserNotFoundException(){
        //ARRANGE
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());
        //ASSERTIONS + ACT

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUserByID(id));

        verify(userRepository, times(1)).findById(id);
    }


    @Test
    public void deleteUserByIDTest_ThrowsNullPointerException(){
        //ARRANGE
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(null);
        //ASSERTIONS + ACT

        Assertions.assertThrows(NullPointerException.class, () -> userService.deleteUserByID(id));

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void updateUserByID_ReturnUserUpdated(){
        //ARRANGE
        UUID id = UUID.randomUUID();
        UserUpdateDto userUpdateDto = new UserUpdateDto("Isabella", "asdasd");

        UserEntity user = new UserEntity("435304985", "Arthur", "asdjah", "123");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        //ACT

        UserEntity returnedUser = userService.updateUserByID(id, userUpdateDto);

        //ASSERTIONS

        Assertions.assertNotNull(returnedUser);
        Assertions.assertEquals("Isabella", user.getName());
        Assertions.assertEquals("asdasd", user.getEmail());

        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void updateUserByIDTest_ThrowsUserNotFoundException(){
        UUID id = UUID.randomUUID();

        UserUpdateDto userUpdateDto = new UserUpdateDto("Arthur", "arthur");

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUserByID(id, userUpdateDto));

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void updateUserByIDTest_ThrowsUserNotSavedException(){
        UUID id = UUID.randomUUID();

        UserUpdateDto userUpdateDto = new UserUpdateDto("Arthur", "arthur");

        UserEntity userSubClass = new UserEntity(){};

        when(userRepository.findById(id)).thenReturn(Optional.of(new UserEntity()));
        when(userRepository.save(new UserEntity())).thenReturn(userSubClass);

        Assertions.assertThrows(UserNotSavedException.class, () -> userService.updateUserByID(id, userUpdateDto));

    }

    @Test
    public void updateUserByIDTest_ThrowsNullPointerException(){
        UUID id = UUID.randomUUID();

        UserUpdateDto userUpdateDto = new UserUpdateDto("Arthur", "artur");

        when(userRepository.findById(id)).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () -> userService.updateUserByID(id, userUpdateDto));
    }
}
