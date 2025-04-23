package com.swiftpay.SwiftPay.services;


import com.swiftpay.SwiftPay.dto.RequestDto.TransactionRequestDto;
import com.swiftpay.SwiftPay.entity.Transaction;
import com.swiftpay.SwiftPay.entity.Wallet;
import com.swiftpay.SwiftPay.enums.TransactionStatus;
import com.swiftpay.SwiftPay.enums.TransactionType;
import com.swiftpay.SwiftPay.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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
            System.out.println("Sender doesn't exist");
            return null;
        }

        Wallet receiver = walletService.getWallet(transactionDetails.getReceiverWalletId());
        if(receiver == null){
            System.out.println("Receiver doesn't exist");
            return null;
        }

        // Ensure sender has enough balance.
        if(sender.getBalance() < transactionDetails.getAmount()){
            System.out.println("Insufficient balance");
            return null;
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
            System.out.println("Wallet Not found");
            return null;
        }

        return transactions.findHistoryByWallet(wallet);
    }
}
