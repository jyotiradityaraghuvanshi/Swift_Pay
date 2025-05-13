package com.swiftpay.SwiftPay.controllers;


import com.swiftpay.SwiftPay.dto.RequestDto.WalletRequestDto;
import com.swiftpay.SwiftPay.dto.ResponseDto.WalletResponseDto;
import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.entity.Wallet;
import com.swiftpay.SwiftPay.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v7/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;


    // 🔐 Only allow wallet creation if the requester owns the userId
    @PreAuthorize("principal == @userService.getUserEmailById(#walletRequestDto.userId) or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<WalletResponseDto> createsWallet(@RequestBody WalletRequestDto walletRequestDto){
        return new ResponseEntity<>(walletService.createWallet(walletRequestDto) , HttpStatus.CREATED);
    }

    // 🔐 User can only access their own wallet
    @PreAuthorize("principal == @walletService.getUserEmailByWalletId(#id) or hasRole('ADMIN')")
    @GetMapping("/getWallet/{walletId}")
    public ResponseEntity<Wallet> getWallets(@PathVariable("walletId") Long id){
        return new ResponseEntity<>(walletService.getWallet(id) , HttpStatus.FOUND);
    }

    // 🔐 User can only access their wallet via userId
    @PreAuthorize("principal == @userService.getUserEmailById(#userId) or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<WalletResponseDto> getWalletByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(walletService.getWalletByUserId(userId) , HttpStatus.FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remove/{walletId}")
    public ResponseEntity<String> deleteWallet(@PathVariable Long walletId){
        return new ResponseEntity<>(walletService.removeWallet(walletId) , HttpStatus.MOVED_PERMANENTLY);
    }

}
