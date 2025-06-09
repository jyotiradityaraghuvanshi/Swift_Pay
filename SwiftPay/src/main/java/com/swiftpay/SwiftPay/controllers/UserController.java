package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.dto.RequestDto.UserLoginRequestDto;
import com.swiftpay.SwiftPay.dto.RequestDto.UserRequestDto;
import com.swiftpay.SwiftPay.dto.RequestDto.UserUpdateRequestDto;
import com.swiftpay.SwiftPay.dto.ResponseDto.UserResponseDto;
import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v7/user")
public class UserController {

    @Autowired
    private UserService userService;

//    @PostMapping("/create") // this API is not necessary but if in future admin may require it.
//    public ResponseEntity<UserResponseDto> createsUser(@Valid @RequestBody UserRequestDto user RequestDto){
//        return new ResponseEntity<>(userService.createUser(userRequestDto) , HttpStatus.CREATED);
//    }

    // 🔐 Only allow user to access their own data
    @PreAuthorize("principal == @userService.getUserEmailById(#id) or hasRole('ADMIN')")
    @GetMapping("/getUser")
    public ResponseEntity<UserResponseDto> getUser(@RequestParam Long id){
        return new ResponseEntity<>(userService.getUser(id) , HttpStatus.FOUND);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserResponseDto>> getAllUsers( // added pagination feature.
            @RequestParam(value = "pageNumber" , defaultValue = "0" , required = false) Integer pageNumber,
            @RequestParam(value = "pageSize" , defaultValue = "3" , required = false) Integer pageSize){
        return new ResponseEntity<>(userService.getAllUser(pageNumber , pageSize) , HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email){
        return new ResponseEntity<>(userService.getUserByEmails(email) , HttpStatus.FOUND);
    }

    @PreAuthorize("principal == @userService.getUserEmailById(#userId) or hasRole('ADMIN')")
    @PatchMapping("/update/{userId}")
    public ResponseEntity<UserResponseDto> updateUserDetails(@PathVariable Long userId , @RequestBody UserUpdateRequestDto userUpdateRequestDto){
        return new ResponseEntity<>(userService.updateUserProfile(userId , userUpdateRequestDto) , HttpStatus.ACCEPTED);
    }

}
