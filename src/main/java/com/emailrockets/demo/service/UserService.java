package com.emailrockets.demo.service;

import com.emailrockets.demo.dto.CreateUserRequest;
import com.emailrockets.demo.dto.UpdateUserRequest;
import com.emailrockets.demo.dto.UserDto;
import com.emailrockets.demo.dto.UserDtoConverter;
import com.emailrockets.demo.model.UserInformation;
import com.emailrockets.demo.repository.UserInformationRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserInformationRepository userInformationRepository;
    private final UserDtoConverter userDtoConverter;

    public UserService(UserInformationRepository userInformationRepository, UserDtoConverter userDtoConverter) {
        this.userInformationRepository = userInformationRepository;
        this.userDtoConverter = userDtoConverter;
    }

    public List<UserDto> getAllUsers() {
        return userInformationRepository.findAll().stream().map(userDtoConverter::convert).collect(Collectors.toList());
    }

    public UserDto getUserByMail(String mail) {
        UserInformation userInformation = findUserByMail(mail);
        return userDtoConverter.convert(userInformation);
    }



    public UserDto createUser(CreateUserRequest userRequest) {
        UserInformation userInformation = new UserInformation(userRequest.getMail(),
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getMiddleName());

        return userDtoConverter.convert(userInformationRepository.save(userInformation));
    }

    public UserDto updateUser(String mail, UpdateUserRequest updateUserRequest) {
        UserInformation userInformation = findUserByMail(mail);
        UserInformation updatedUser = new UserInformation(userInformation.getId(),
                userInformation.getMail(),
                updateUserRequest.getFirstName(),
                updateUserRequest.getLastName(),
                updateUserRequest.getMiddleName());
        return userDtoConverter.convert(userInformationRepository.save(updatedUser));
    }

    private UserInformation findUserByMail(String mail) {
        return userInformationRepository.findByMail(mail).orElseThrow(() -> new UsernameNotFoundException("User could not be found by following mail:" + mail));
    }

    /*public void deactiveUser(Long id) {
    }

    public void deleteUser(Long id) {
    }*/
}
