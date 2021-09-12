package com.emailrockets.demo.service;

import com.emailrockets.demo.dto.CreateUserRequest;
import com.emailrockets.demo.dto.UpdateUserRequest;
import com.emailrockets.demo.dto.UserDto;
import com.emailrockets.demo.dto.UserDtoConverter;
import com.emailrockets.demo.exception.UserIsNotActiveException;
import com.emailrockets.demo.exception.UserNotFoundException;
import com.emailrockets.demo.model.UserInformation;
import com.emailrockets.demo.repository.UserInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserInformationRepository userInformationRepository;
    private final UserDtoConverter userDtoConverter;

    public UserService(UserInformationRepository userInformationRepository, UserDtoConverter userDtoConverter) {
        this.userInformationRepository = userInformationRepository;
        this.userDtoConverter = userDtoConverter;
    }

    public List<UserDto> getAllUsers() {
        return userDtoConverter.convert(userInformationRepository.findAll());
    }

    public UserDto getUserByMail(String mail) {
        UserInformation userInformation = findUserByMail(mail);

        return userDtoConverter.convert(userInformation);
    }

    public UserDto createUser(CreateUserRequest userRequest) {
        UserInformation userInformation = new UserInformation(userRequest.getMail(),
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getMiddleName(),
                false);

        return userDtoConverter.convert(userInformationRepository.save(userInformation));
    }

    public UserDto updateUser(String mail, UpdateUserRequest updateUserRequest) {
        UserInformation userInformation = findUserByMail(mail);
        if(!userInformation.getActive()){
            logger.warn(String.format("The user wanted update is not active!, user mail: %s", mail));
            throw  new UserIsNotActiveException();
        }
        UserInformation updatedUser = new UserInformation(userInformation.getId(),
                userInformation.getMail(),
                updateUserRequest.getFirstName(),
                updateUserRequest.getLastName(),
                updateUserRequest.getMiddleName(),
                userInformation.getActive());
        return userDtoConverter.convert(userInformationRepository.save(updatedUser));
    }

    public void deactivateUser(Long id) {
       changeActivatedUser(id, false);
    }

    public void activateUser(Long id) {
        changeActivatedUser(id, true);
    }

    private void changeActivatedUser(Long id, Boolean isActive){
        UserInformation userInformation = findUserById(id);

        UserInformation updatedUser = new UserInformation(userInformation.getId(),
                userInformation.getMail(),
                userInformation.getFirstName(),
                userInformation.getLastName(),
                userInformation.getMiddleName(),
                true);
        userInformationRepository.save(updatedUser);
    }

    public void deleteUser(Long id) {
        findUserById(id);

        userInformationRepository.deleteById(id);
    }

    private UserInformation findUserByMail(String mail) {
        return userInformationRepository.findByMail(mail)
                .orElseThrow(() -> new UserNotFoundException("User could not be found by following mail:" + mail));
    }

    private UserInformation findUserById(Long id) {
        return userInformationRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User could not be found by following ID:" + id));
    }

}
