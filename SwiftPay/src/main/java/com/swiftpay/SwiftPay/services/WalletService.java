package com.swiftpay.SwiftPay.services;


import com.swiftpay.SwiftPay.Exception.ResourceNotFoundException;
import com.swiftpay.SwiftPay.Exception.UserNotFoundException;
import com.swiftpay.SwiftPay.Exception.WalletNotFoundException;
import com.swiftpay.SwiftPay.dto.RequestDto.WalletRequestDto;
import com.swiftpay.SwiftPay.dto.ResponseDto.WalletResponseDto;
import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.entity.Wallet;
import com.swiftpay.SwiftPay.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserService userService;

    public WalletResponseDto createWallet(WalletRequestDto walletRequestDto){

        User user = userService.getUserForServices(walletRequestDto.getUserId());
        if(user == null){
            throw new UserNotFoundException("User with id " + walletRequestDto.getUserId() + " does not found");
        }

        if(walletRepository.existsByUser(user)){
            throw new IllegalStateException("Wallet already exists for user " + user.getId());
        }

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(walletRequestDto.getBalance());
        wallet.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        walletRepository.save(wallet);

        return convertEntityToDto(wallet);
    }

    public Wallet getWallet(Long id){
        return walletRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wallet not found for id " + id));
    }

    public WalletResponseDto getWalletByUserId(Long userId) {
        User user = userService.getUserForServices(userId);
        if(user == null) throw new UserNotFoundException("User with id " + userId + " does not exist");

        Optional<Wallet> wallet = walletRepository.findByUser(user);
        if(wallet.isEmpty()){
            throw new WalletNotFoundException("Wallet for this userId " + userId + " does not found");
        }
        return convertEntityToDto(wallet.get());
    }

    public void saveWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }


    private WalletResponseDto convertEntityToDto(Wallet wallet){

        return new WalletResponseDto(
                wallet.getUser().getName(),
                wallet.getBalance(),
                wallet.getUpdatedAt()
        );
    }

}
