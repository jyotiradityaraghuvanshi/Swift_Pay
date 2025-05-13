package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.dto.RequestDto.TransactionRequestDto;
import com.swiftpay.SwiftPay.dto.ResponseDto.TransactionResponseDto;
import com.swiftpay.SwiftPay.entity.Transaction;
import com.swiftpay.SwiftPay.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v7/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    // 🔐 User can only transfer money from their own wallet
    @PreAuthorize("principal == @walletService.getUserEmailByWalletId(#transactionDetails.senderWalletId) or hasRole('ADMIN')")
    @PostMapping("/send")
    public ResponseEntity<Transaction> transferMoney(@RequestBody TransactionRequestDto transactionDetails){
        return new ResponseEntity<>(transactionService.sendMoney(transactionDetails) , HttpStatus.OK);
    }


    // 🔐 Only wallet owner or admin can view transaction history
    @PreAuthorize("principal == @walletService.getUserEmailByWalletId(#walletId) or hasRole('ADMIN')")
    @GetMapping("/history/{walletId}")
    public ResponseEntity<List<Transaction>>  transactionHistory(@PathVariable("walletId") Long walletId){
        return new ResponseEntity<>(transactionService.getHistory(walletId) , HttpStatus.OK);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> viewTransaction(@PathVariable Long transactionId){
        return new ResponseEntity<>(transactionService.viewTransactionDetail(transactionId) , HttpStatus.OK);
    }

}
