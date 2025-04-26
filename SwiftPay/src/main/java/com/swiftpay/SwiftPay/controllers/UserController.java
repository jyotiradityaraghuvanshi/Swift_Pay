package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.dto.RequestDto.UserRequestDto;
import com.swiftpay.SwiftPay.dto.ResponseDto.UserResponseDto;
import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v7/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createsUser(@Valid @RequestBody UserRequestDto userRequestDto){
        return new ResponseEntity<>(userService.createUser(userRequestDto) , HttpStatus.CREATED);
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserResponseDto> getUser(@RequestParam Long id){
        return new ResponseEntity<>(userService.getUser(id) , HttpStatus.FOUND);
    }


    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUser() , HttpStatus.OK);
    }

}
