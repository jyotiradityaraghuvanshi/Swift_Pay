package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.entity.Wallet;
import com.swiftpay.SwiftPay.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v7/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;


    @PostMapping("/create")
    public Wallet createsWallet(@RequestParam Long id){
        return walletService.createWallet(id);
    }

    @GetMapping("/getWallet/{userId}")
    public Wallet getWallets(@PathVariable("userId") Long id){
        return walletService.getWallet(id);
    }

}
