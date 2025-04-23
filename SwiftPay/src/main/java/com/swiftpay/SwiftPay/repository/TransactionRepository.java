package com.swiftpay.SwiftPay.repository;

import com.swiftpay.SwiftPay.entity.Transaction;
import com.swiftpay.SwiftPay.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction , Long> {

    @Query(value = "SELECT t FROM Transaction t WHERE t.senderWallet=:wallet OR t.receiverWallet=:wallet ORDER BY t.createdAt DESC" , nativeQuery = false)
    List<Transaction> findHistoryByWallet(@Param("wallet")Wallet wallet);

}
