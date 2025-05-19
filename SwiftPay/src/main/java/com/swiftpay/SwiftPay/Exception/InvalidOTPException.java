package com.swiftpay.SwiftPay.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidOTPException extends RuntimeException{

    public InvalidOTPException(String message){
        super(message);
    }

}
