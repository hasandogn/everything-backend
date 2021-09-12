package com.emailrockets.demo.dto;

import com.emailrockets.demo.model.UserInformation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDtoConverter {
    public UserDto convert(UserInformation from){
        return new UserDto(from.getMail(), from.getFirstName(), from.getLastName(), from.getMiddleName());
    }

    //Daha kolay test edebilmek için yazılıyor
    public List<UserDto> convert(List<UserInformation> fromList){
        return fromList.stream().map(from -> new UserDto(from.getMail(), from.getFirstName(), from.getLastName(), from.getMiddleName()))
                .collect(Collectors.toList());
    }
}
