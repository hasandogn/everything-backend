package com.emailrockets.demo.dto;

import com.emailrockets.demo.model.UserInformation;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {
    public UserDto convert(UserInformation from){
        return new UserDto(from.getMail(), from.getFirstName(), from.getLastName(), from.getMiddleName());
    }
}
