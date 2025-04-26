package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.dto.RequestDto.WalletRequestDto;
import com.swiftpay.SwiftPay.dto.ResponseDto.WalletResponseDto;
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
    public ResponseEntity<WalletResponseDto> createsWallet(@RequestBody WalletRequestDto walletRequestDto){
        return new ResponseEntity<>(walletService.createWallet(walletRequestDto) , HttpStatus.CREATED);
    }

    @GetMapping("/getWallet/{walletId}")
    public ResponseEntity<Wallet> getWallets(@PathVariable("walletId") Long id){
        return new ResponseEntity<>(walletService.getWallet(id) , HttpStatus.FOUND);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WalletResponseDto> getWalletByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(walletService.getWalletByUserId(userId) , HttpStatus.FOUND);
    }

}
