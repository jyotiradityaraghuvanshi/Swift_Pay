package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.entity.Wallet;
import com.swiftpay.SwiftPay.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v7/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;


    @PostMapping("/create")
    public ResponseEntity<Wallet> createsWallet(@RequestParam Long id){
        return new ResponseEntity<>(walletService.createWallet(id) , HttpStatus.CREATED);
    }

    @GetMapping("/getWallet/{userId}")
    public ResponseEntity<Wallet> getWallets(@PathVariable("userId") Long id){
        return new ResponseEntity<>(walletService.getWallet(id) , HttpStatus.FOUND);
    }

}
