package com.emailrockets.demo;

import com.emailrockets.demo.dto.UserDto;
import com.emailrockets.demo.model.UserInformation;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Sahte veriler burada oluşacak
public class TestSupport {
    public static Long userId = 100L;

    public static List<UserInformation> generateUsers(){
        return IntStream.range(0,5).mapToObj(i ->
                new UserInformation((long) i,
                    i +  "@emailprojects.com",
                    "firstName" + i,
                    "lastName" + i,
                    "",
                    new Random(2).nextBoolean())
        ).collect(Collectors.toList());
    }

    public static List<UserDto> generateUserDtoList(List<UserInformation> userList){
        return userList.stream().map(from -> new UserDto(from.getMail(), from.getFirstName(), from.getLastName(), from.getMiddleName()))
                .collect(Collectors.toList());
    }

    public static UserInformation generateUser(String mail){
        return new UserInformation(userId,
                 mail,
                "firstName" + userId,
                "lastName" + userId,
                "",
                true);
    }

    public static UserDto generateUserDto(String mail){
        return new UserDto(mail, "firstName" + userId, "lastName" + userId, "");
    }
}
