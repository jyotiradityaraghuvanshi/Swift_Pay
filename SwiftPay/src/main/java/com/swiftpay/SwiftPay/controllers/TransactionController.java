package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.dto.RequestDto.TransactionRequestDto;
import com.swiftpay.SwiftPay.entity.Transaction;
import com.swiftpay.SwiftPay.entity.Wallet;
import com.swiftpay.SwiftPay.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v7/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @PostMapping("/send")
    public Transaction transferMoney(@RequestBody TransactionRequestDto transactionDetails){
        return transactionService.sendMoney(transactionDetails);
    }


    @GetMapping("/history/{walletId}")
    public List<Transaction>  transactionHistory(@PathVariable("walletId") Long walletId){
        return transactionService.getHistory(walletId);
    }

}
