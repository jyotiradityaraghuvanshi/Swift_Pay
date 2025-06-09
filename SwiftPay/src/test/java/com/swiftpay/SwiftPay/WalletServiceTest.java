package com.swiftpay.SwiftPay;

import com.swiftpay.SwiftPay.dto.RequestDto.WalletRequestDto;
import com.swiftpay.SwiftPay.dto.ResponseDto.WalletResponseDto;
import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.entity.Wallet;
import com.swiftpay.SwiftPay.repository.WalletRepository;
import com.swiftpay.SwiftPay.services.UserService;
import com.swiftpay.SwiftPay.services.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.postgresql.hostchooser.HostRequirement.any;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void testCreateWallet_TestWalletCreation(){

        User user = new User(1L , "Test" , "test@gmail.com" );
        WalletRequestDto walletRequestDto = new WalletRequestDto(1L , 10000.0);

        Wallet wallet = new Wallet(1L , user , 10000.0  , new Timestamp(System.currentTimeMillis()));
        WalletResponseDto expected = new WalletResponseDto("Test" , 10000.0 , new Timestamp(System.currentTimeMillis()));

        Mockito.when(userService.getUserForServices(1L)).thenReturn(user);
        Mockito.when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        WalletResponseDto actual = walletService.createWallet(walletRequestDto);

        Assertions.assertEquals(expected.getBalance() , actual.getBalance());
//        Assertions.assertEquals(expected.getUserName() , actual.getUserName());

    }

}
