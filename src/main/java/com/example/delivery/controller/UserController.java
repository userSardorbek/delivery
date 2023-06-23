package com.example.delivery.controller;

import com.example.delivery.payload.LogInDto;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.payload.UserDto;
import com.example.delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestParam String name, @RequestParam String username, @RequestParam String password) {
        ResponseMessage message = userService.addUser(name, username, password);
        return ResponseEntity.status(message.isSuccess() ? 201 : 409).body(message.getMessage());
    }

    @PutMapping("/logIn")
    public ResponseEntity<?> logIn(@RequestParam String username, @RequestParam String password) {
        ResponseMessage message = userService.logIn(username, password);
        return ResponseEntity.status(message.isSuccess() ? 200 : 409).body(message);
    }
}
