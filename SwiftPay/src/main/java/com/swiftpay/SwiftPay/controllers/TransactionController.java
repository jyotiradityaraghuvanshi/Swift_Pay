package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.dto.RequestDto.TransactionRequestDto;
import com.swiftpay.SwiftPay.entity.Transaction;
import com.swiftpay.SwiftPay.entity.Wallet;
import com.swiftpay.SwiftPay.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v7/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @PostMapping("/send")
    public ResponseEntity<Transaction> transferMoney(@RequestBody TransactionRequestDto transactionDetails){
        return new ResponseEntity<>(transactionService.sendMoney(transactionDetails) , HttpStatus.OK);
    }


    @GetMapping("/history/{walletId}")
    public ResponseEntity<List<Transaction>>  transactionHistory(@PathVariable("walletId") Long walletId){
        return new ResponseEntity<>(transactionService.getHistory(walletId) , HttpStatus.OK);
    }

}
