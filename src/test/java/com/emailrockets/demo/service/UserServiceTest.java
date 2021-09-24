package com.emailrockets.demo.service;

import com.emailrockets.demo.TestSupport;
import com.emailrockets.demo.dto.CreateUserRequest;
import com.emailrockets.demo.dto.UpdateUserRequest;
import com.emailrockets.demo.dto.UserDto;
import com.emailrockets.demo.dto.UserDtoConverter;
import com.emailrockets.demo.exception.UserIsNotActiveException;
import com.emailrockets.demo.exception.UserNotFoundException;
import com.emailrockets.demo.model.UserInformation;
import com.emailrockets.demo.repository.UserInformationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.*;
import static org.mockito.Mockito.when;

// static kullanmak istemezsek bu anotationu kullanabilriiz.
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceTest extends TestSupport {

    // Burada @mock anotasyonuyla belirtmememizin sebebi service sınıfımızın içerisinde @Authowaired anatasyonuyla inject etmememizdir.
    // Private final ile oluşturduğumuz için burada da @Mock kullanmak mantıklı olamyacak.
    // Eğer yine setUp() methodunu oluşturmayalım istiyorsak converter ve repository üstüne @mock ekleyip userService üzerine @InjectMocks ile yapabilriiz bu işlei
    private UserDtoConverter converter;
    private UserInformationRepository repository;

    private UserService userService;

    // Bu anatosyonlar bu test her zaman çalışacak.
    // mocaklar tazelenmediği sürece kalabiliyor ama böylece kalmayacak.
    // Before all ise tüm test caseler başlamadan önce çalışır ve case içerisinde ne yapıyorsan onu kullanır
    // BeforeAll genelde integration testlerde kullanılıyor. Örnek verilerini yazıp sonra beforeeachte setup yapıpı daha sonra testler çalışıyor
    // Birde TierDown var @afterAll ve @AfterEach testten sonra çalışanlar.
    // Beforecase ve beforeall arasındaki farka bak.

    // Before Each her seferinde mock dataları temizler
    @BeforeEach
    public void setUp() {
        converter = Mockito.mock(UserDtoConverter.class);
        repository = Mockito.mock(UserInformationRepository.class);

        userService = new UserService(repository, converter);
    }

    @Test
    public void testGetAllUsers_itShouldReturnUserDtoList(){
        List<UserInformation> userList = generateUsers();
        List<UserDto> userDtoList = generateUserDtoList(userList);

        // FindAll() metodu userList tipinde dönecek.
        when(repository.findAll()).thenReturn(userList);
        when(converter.convert(userList)).thenReturn(userDtoList);

        List<UserDto> result = userService.getAllUsers();

        assertEquals(userDtoList, result);

        // Static methodları ekledik okumayı koalylaştırdığı için. (bu kısımlarla alakalaı olamyabilir.)
        // Altta çağırdığımız yukarıdaki fonksiyoların istediğimiz yerden çağırıp çağırmadığını kontrol ediyorum.
        Mockito.verify(repository).findAll();
        Mockito.verify(converter).convert(userList);
    }

    @Test
    public void testGetAllUsers_whenUserMailExist_itShouldReturnUserDto(){
        String mail = "mail@mailproject.com";
        UserInformation user = generateUser(mail);
        UserDto userDto = generateUserDto(mail);

        when(repository.findByMail(mail)).thenReturn(Optional.of(user));
        when(converter.convert(user)).thenReturn(userDto);

        UserDto result = userService.getUserByMail(mail);

        assertEquals(userDto, result);
        Mockito.verify(repository).findByMail(mail);
        Mockito.verify(converter).convert(user);
    }

    @Test
    public void testGetAllUsers_whenUserMailDoesNotExist_itShouldThrowUserNotException(){
        String mail = "mail@mailproject.com";


        when(repository.findByMail(mail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getUserByMail(mail)
        );


        Mockito.verify(repository).findByMail(mail);
        Mockito.verifyNoInteractions(converter);
    }

    @Test
    public void testCreateUser_itShouldCreatedUserDto(){
        String mail = "mail@mailproject.com";
        CreateUserRequest request = new CreateUserRequest(mail, "firstName", "lastName", "");
        UserInformation user = new UserInformation(mail,"firstName", "lastName", "", false );
        UserInformation savedUser = new UserInformation(1L, mail,  "firstName", "lastName", "", false);
        UserDto userDto = new UserDto(mail,  "firstName", "lastName", "");

        when(repository.save(user)).thenReturn(savedUser);
        when(converter.convert(savedUser)).thenReturn(userDto);

        UserDto result = userService.createUser(request);

        assertEquals(userDto, result);

        Mockito.verify(repository).save(user);
        Mockito.verify(converter).convert(savedUser);
    }

    @Test
    public void testUpdateUser_whenUserMailExistAndUserIsActive_itShouldUpdatedUserDto(){
        String mail = "mail@mailproject.com";
        UpdateUserRequest request = new UpdateUserRequest( "firstName2", "lastName2", "middleName");
        UserInformation user = new UserInformation(1L, mail,"firstName", "lastName", "", true );
        UserInformation updatedUser = new UserInformation(1L, mail,"firstName2", "lastName2", "middleName", true );
        UserInformation savedUser = new UserInformation(1L, mail,  "firstName2", "lastName2", "middleName", true);
        UserDto userDto = new UserDto(mail,  "firstName2", "lastName2", "middleName");

        when(repository.findByMail(mail)).thenReturn(Optional.of(user));
        when(repository.save(updatedUser)).thenReturn(savedUser);
        when(converter.convert(savedUser)).thenReturn(userDto);

        UserDto result = userService.updateUser(mail, request);

        assertEquals(userDto, result);

        Mockito.verify(repository).findByMail(mail);
        Mockito.verify(repository).save(updatedUser);
        Mockito.verify(converter).convert(savedUser);
    }

    @Test
    public void testUpdateUser_whenUserMailDoesNotExist_itShouldThrowUserNotFoundException(){
        String mail = "mail@mailproject.com";
        UpdateUserRequest request = new UpdateUserRequest( "firstName2", "lastName2", "middleName");

        when(repository.findByMail(mail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.updateUser(mail, request)
        );

        Mockito.verify(repository).findByMail(mail);
        Mockito.verifyNoMoreInteractions(repository);
        // converteri hiç çağırmadığımız kontrolü
        Mockito.verifyNoInteractions(converter);
    }

    @Test
    public void testUpdateUser_whenUserMailExistButUserIsNotActive_itShouldThrowUserNotActiveException(){
        String mail = "mail@mailproject.com";
        UpdateUserRequest request = new UpdateUserRequest( "firstName2", "lastName2", "middleName");
        UserInformation user = new UserInformation(1L, mail,"firstName", "lastName", "", false );

        when(repository.findByMail(mail)).thenReturn(Optional.of(user));

        assertThrows(UserIsNotActiveException.class, () ->
                userService.updateUser(mail, request)
        );

        Mockito.verify(repository).findByMail(mail);
        Mockito.verifyNoMoreInteractions(repository);
        // converteri hiç çağırmadığımız kontrolü
        Mockito.verifyNoInteractions(converter);
    }


    /*@Test
    public void testDeactivateUser_whenUserIdExist_itShouldUpdateUserByActiveFalse(){

        String mail = "mail@mailproject.com";
        UserInformation user = new UserInformation(userId, mail,"firstName", "lastName", "", true );
        UserInformation savedUser = new UserInformation(userId, mail,"firstName", "lastName", "", false );

        when(repository.findById(userId)).thenReturn(Optional.of(user));

        userService.deactivateUser(userId);

        Mockito.verify(repository).findById(userId);
        Mockito.verify(repository).save(savedUser);
    }*/

    @Test
    public void testDeactivateUser_whenUserIdDoesNotExist_itShouldThrowsUSerNotFoundException(){
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
            userService.deactivateUser(userId)
        );

        Mockito.verify(repository).findById(userId);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    public void testActivateUser_whenUserIdExist_itShouldUpdateUserByActiveTrue(){

        String mail = "mail@mailproject.com";
        UserInformation user = new UserInformation(userId, mail,"firstName", "lastName", "", false );
        UserInformation savedUser = new UserInformation(userId, mail,"firstName", "lastName", "", true );

        when(repository.findById(userId)).thenReturn(Optional.of(user));

        userService.activateUser(userId);

        Mockito.verify(repository).findById(userId);
        Mockito.verify(repository).save(savedUser);
    }

    @Test
    public void testActivateUser_whenUserIdDoesNotExist_itShouldThrowsUserNotFoundException(){
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.activateUser(userId)
        );

        Mockito.verify(repository).findById(userId);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    public void testDeleteUser_whenUserIdExist_itShouldDeleteUser(){
        UserInformation user = new UserInformation(userId, "mail@mailproject.com","firstName", "lastName", "", false );

        when(repository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        Mockito.verify(repository).findById(userId);
        Mockito.verify(repository).deleteById(userId);
    }

    @Test
    public void testDeleteUser_whenUserIdDoesNotExist_itShouldThrowsUserNotFoundException(){
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.deleteUser(userId)
        );

        Mockito.verify(repository).findById(userId);
        Mockito.verifyNoMoreInteractions(repository);
    }


}