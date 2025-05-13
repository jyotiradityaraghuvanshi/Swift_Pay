package com.swiftpay.SwiftPay.services;


import com.swiftpay.SwiftPay.Exception.InsufficientBalanceException;
import com.swiftpay.SwiftPay.Exception.ResourceNotFoundException;
import com.swiftpay.SwiftPay.dto.RequestDto.TransactionRequestDto;
import com.swiftpay.SwiftPay.dto.ResponseDto.TransactionResponseDto;
import com.swiftpay.SwiftPay.entity.Transaction;
import com.swiftpay.SwiftPay.entity.Wallet;
import com.swiftpay.SwiftPay.enums.TransactionStatus;
import com.swiftpay.SwiftPay.enums.TransactionType;
import com.swiftpay.SwiftPay.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactions;

    @Autowired
    private WalletService walletService;

    public Transaction sendMoney(TransactionRequestDto transactionDetails) {

        // Check if both wallets exist.
        Wallet sender = walletService.getWallet(transactionDetails.getSenderWalletId());
        if(sender == null){
            throw new ResourceNotFoundException("Sender does not found");
        }

        Wallet receiver = walletService.getWallet(transactionDetails.getReceiverWalletId());
        if(receiver == null){
            throw new ResourceNotFoundException("Receiver does not found");
        }

        // Ensure sender has enough balance.
        if(sender.getBalance() < transactionDetails.getAmount()){
            throw new InsufficientBalanceException("Insufficient funds");
        }

        // Deduct from sender, add to receiver.
        sender.setBalance(sender.getBalance() - transactionDetails.getAmount());
        receiver.setBalance(receiver.getBalance() + transactionDetails.getAmount());

        walletService.saveWallet(sender);
        walletService.saveWallet(receiver);

        // Save transaction with SUCCESS or FAILED based on outcome.
        Transaction transaction = new Transaction();
        transaction.setSenderWallet(sender);
        transaction.setReceiverWallet(receiver);
        transaction.setAmount(transactionDetails.getAmount());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return transactions.save(transaction);
    }

    public List<Transaction> getHistory(Long walletId) {

        Wallet wallet = walletService.getWallet(walletId);
        if(wallet == null){
            throw new ResourceNotFoundException("Wallet does not exist for id " + walletId);
        }

        return transactions.findHistoryByWallet(wallet);
    }

    public TransactionResponseDto viewTransactionDetail(Long transactionId) {

        Optional<Transaction> optionalTransaction = transactions.findById(transactionId);
        if (optionalTransaction.isEmpty())
            throw new ResourceNotFoundException("Transaction cannot be found");

        return convertEntityToDto(optionalTransaction.get());
    }


    private static TransactionResponseDto convertEntityToDto(Transaction transaction){
         return new TransactionResponseDto(
                 transaction.getId(),
                 transaction.getSenderWallet().getId(),
                 transaction.getReceiverWallet().getId(),
                 transaction.getAmount(),
                 transaction.getCreatedAt()
         );
    }

}
