package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v7/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createsUser(@RequestBody User user){
        return new ResponseEntity<>(userService.createUser(user) , HttpStatus.CREATED);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam Long id){
        return new ResponseEntity<>(userService.getUser(id) , HttpStatus.FOUND);
    }

}
