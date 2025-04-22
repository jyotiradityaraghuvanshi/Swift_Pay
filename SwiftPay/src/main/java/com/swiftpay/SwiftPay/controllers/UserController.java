package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v7/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User createsUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping("/getUser")
    public User getUser(@RequestParam Long id){
        return userService.getUser(id);
    }

}
