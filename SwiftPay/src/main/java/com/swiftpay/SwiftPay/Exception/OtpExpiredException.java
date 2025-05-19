package com.swiftpay.SwiftPay.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
public class OtpExpiredException extends RuntimeException{

    public OtpExpiredException(String message){
        super(message);
    }

}
