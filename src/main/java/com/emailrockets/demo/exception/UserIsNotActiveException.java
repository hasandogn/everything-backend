package com.emailrockets.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIsNotActiveException extends RuntimeException{
    private static String message = "The user wanted update is not active!";

    public UserIsNotActiveException(){
        super(message);
    }
    public UserIsNotActiveException(String message) {
        super(message);
    }
}
