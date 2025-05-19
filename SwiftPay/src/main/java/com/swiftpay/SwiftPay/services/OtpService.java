package com.swiftpay.SwiftPay.services;


import com.swiftpay.SwiftPay.Exception.InvalidOTPException;
import com.swiftpay.SwiftPay.Exception.OtpExpiredException;
import com.swiftpay.SwiftPay.Exception.ResourceNotFoundException;
import com.swiftpay.SwiftPay.Exception.UserNotFoundException;
import com.swiftpay.SwiftPay.entity.OtpVerification;
import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.repository.OtpRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;


    public void sendOtpToEmail(String email){

        User user = userService.ensureUserExist(email);
        if(user == null) throw new UserNotFoundException("User does not found for this email " + email);

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);

        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setOtp(otp);
        otpVerification.setEmail(email);
        otpVerification.setExpiryTime(expiryTime);

        otpRepository.save(otpVerification);

        // send the otp in mail
        Map<String , Object> variables = new HashMap<>();
        variables.put("name" , user.getName());
        variables.put("otp" , otp);

        mailService.sendEmail(email
                , "Password Verification Code"
                , "otp-verification"
                , variables);
    }

    @Transactional
    public boolean verifyOtp(String email , String userOtp){

        Optional<OtpVerification> optionalOtpVerification = otpRepository.findByEmail(email);

        if(optionalOtpVerification.isEmpty()){
            throw new ResourceNotFoundException("OTP cannot be found for the email " + email);
        }

        OtpVerification otp = optionalOtpVerification.get();
        if(otp.getExpiryTime().isBefore(LocalDateTime.now())){
            throw new OtpExpiredException("The provided OTP is expired please send request to get another OTP");
        }
        if(!otp.getOtp().equals(userOtp)){
            throw new InvalidOTPException("OTP is Invalid");
        }

        otpRepository.delete(otp); // if the OTP is correct delete it to make it only one time use

        return true; // otp is successfully verified.
    }

    public void updatePassword(String email, String newPassword) {
        userService.updatePassword(email , newPassword);
    }
}
