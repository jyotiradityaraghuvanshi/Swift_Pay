package com.swiftpay.SwiftPay;

import com.swiftpay.SwiftPay.dto.RequestDto.TransactionRequestDto;
import com.swiftpay.SwiftPay.entity.Transaction;
import com.swiftpay.SwiftPay.entity.Wallet;
import com.swiftpay.SwiftPay.enums.TransactionStatus;
import com.swiftpay.SwiftPay.enums.TransactionType;
import com.swiftpay.SwiftPay.repository.TransactionRepository;
import com.swiftpay.SwiftPay.services.TransactionService;
import com.swiftpay.SwiftPay.services.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private WalletService walletService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testSendMoney_TransferSuccessful(){

        Wallet sender = new Wallet();
        sender.setId(1L);
        sender.setBalance(10000.0);

        Wallet receiver = new Wallet();
        receiver.setId(2L);
        receiver.setBalance(5000.0);

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto(1L , 2L , 2500.0);

        Transaction saveTx = new Transaction();
        saveTx.setId(10L);
        saveTx.setSenderWallet(sender);
        saveTx.setReceiverWallet(receiver);
        saveTx.setAmount(2500.0);
        saveTx.setStatus(TransactionStatus.SUCCESS);
        saveTx.setType(TransactionType.TRANSFER);

        Mockito.when(walletService.getWallet(1L)).thenReturn(sender);
        Mockito.when(walletService.getWallet(2L)).thenReturn(receiver);
        Mockito.when(transactionRepository.save(any(Transaction.class))).thenReturn(saveTx);

        Transaction actual = transactionService.sendMoney(transactionRequestDto);

        Assertions.assertEquals(TransactionStatus.SUCCESS, actual.getStatus());
        Assertions.assertEquals(2500.0 , actual.getAmount());

    }

}
