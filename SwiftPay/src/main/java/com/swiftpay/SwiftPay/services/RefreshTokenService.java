package com.swiftpay.SwiftPay.services;


import com.swiftpay.SwiftPay.Exception.TokenExpiredException;
import com.swiftpay.SwiftPay.Exception.UserNotFoundException;
import com.swiftpay.SwiftPay.entity.RefreshToken;
import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserService userService;


    public RefreshToken createRefreshToken(User user){

//        User user = userService.getUserForServices(userId);
//        if(user == null) throw new UserNotFoundException("User Not found");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token){

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if(refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new TokenExpiredException("Refresh Token Time out");
        }

        return refreshToken;
    }

    @Transactional
    public void deleteTokenByUserId(Long userId){
        User user = userService.getUserForServices(userId);
        if(user == null) {
            throw new UserNotFoundException("User Not found for the id " + userId);
        }
        refreshTokenRepository.deleteByUser(user);
    }

    // this method handle the situation where user again tries to re-login and get another refresh token
    @Transactional
    public RefreshToken replaceRefreshToken(User user){

        Optional<RefreshToken> optional = refreshTokenRepository.findByUser(user);
        optional.ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));

        return createRefreshToken(user);
    }

}
