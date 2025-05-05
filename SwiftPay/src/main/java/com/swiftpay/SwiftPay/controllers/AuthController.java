package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.dto.RequestDto.UserLoginRequestDto;
import com.swiftpay.SwiftPay.entity.RefreshToken;
import com.swiftpay.SwiftPay.services.RefreshTokenService;
import com.swiftpay.SwiftPay.services.UserService;
import com.swiftpay.SwiftPay.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto){
        return new ResponseEntity<>(userService.loginUser(userLoginRequestDto) , HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String , String> request){
        String refreshTokenRequest = request.get("refreshToken");
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest);

        String newAccessToken = JwtUtil.generateToken(refreshToken.getUser().getEmail() , refreshToken.getUser().getRole());

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);

        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam Long userId){
        refreshTokenService.deleteTokenByUserId(userId);
        return new ResponseEntity<>("Logged Out Successfully" , HttpStatus.MOVED_PERMANENTLY);
    }

}
