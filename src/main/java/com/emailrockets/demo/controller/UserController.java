package com.emailrockets.demo.controller;

import com.emailrockets.demo.dto.CreateUserRequest;
import com.emailrockets.demo.dto.UpdateUserRequest;
import com.emailrockets.demo.dto.UserDto;
import com.emailrockets.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{mail}")
    public ResponseEntity<UserDto> getUserByMail(@PathVariable String mail){
        return ResponseEntity.ok(userService.getUserByMail(mail));
    }

    // Patch olsaydı bu usera ait parametre vermiyorum sadece id veriyorum bu endpoint userin idsini alarak user ile ilgili değişiklik  ypaar.
    // Yani patch ile body göndermiyoruz sadece id ile halletmiş oluyoruz
    // body ile userda bu güncellemeler olaak deriz id ilede bu userda güncelleme yapılacak demiş oluyoruz.
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest userRequest){
        return ResponseEntity.ok(userService.createUser(userRequest)); //201
    }

    @PutMapping("/{mail}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String mail,
            @RequestBody UpdateUserRequest updateUserRequest){
        return ResponseEntity.ok(userService.updateUser(mail, updateUserRequest)); //202
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> deactiveUser(@PathVariable("id") Long id){
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> activeUser(@PathVariable("id") Long id){
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
