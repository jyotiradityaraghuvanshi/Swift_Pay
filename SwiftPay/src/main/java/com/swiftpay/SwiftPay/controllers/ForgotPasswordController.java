package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.services.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forgot-password")
public class ForgotPasswordController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/send-OTP")
    public ResponseEntity<String> sendOtp(@RequestParam String email){

        otpService.sendOtpToEmail(email);
        return ResponseEntity.ok("OTP sent to your e-mail, please check your inbox");
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> resetPassword (@RequestParam String email
                                                ,@RequestParam String userOtp
                                                ,@RequestParam String newPassword){

        boolean verify = otpService.verifyOtp(email,userOtp);
        if(!verify) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid E-mail or OTP");
        }

        otpService.updatePassword(email, newPassword);
        return ResponseEntity.ok("Password reset successfully.");
    }

}
