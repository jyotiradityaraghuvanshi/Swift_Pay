package com.swiftpay.SwiftPay.services;


import com.swiftpay.SwiftPay.Exception.ResourceNotFoundException;
import com.swiftpay.SwiftPay.Exception.UserNotFoundException;
import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.entity.Wallet;
import com.swiftpay.SwiftPay.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserService userService;

    public Wallet createWallet(Long userId){

        User user = userService.getUser(userId);
        if(user == null){
            throw new UserNotFoundException("User with id " + userId + " does not found");
        }

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(100000.00);
        wallet.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return walletRepository.save(wallet);
    }

    public Wallet getWallet(Long id){
        return walletRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wallet not found for id " + id));
    }

    public void saveWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }
}
