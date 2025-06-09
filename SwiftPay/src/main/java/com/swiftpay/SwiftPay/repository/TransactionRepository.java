package com.swiftpay.SwiftPay.repository;

import com.swiftpay.SwiftPay.entity.Transaction;
import com.swiftpay.SwiftPay.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction , Long> {

    @Query(value = "SELECT t FROM Transaction t WHERE t.senderWallet=:wallet OR t.receiverWallet=:wallet ORDER BY t.createdAt DESC" , nativeQuery = false)
    Page<Transaction> findHistoryByWallet(@Param("wallet")Wallet wallet , Pageable pageable); // this method is redundant for now but maybe used in future
    // This method is example for showing the control which native query give us by writing custom SQL queries. */

    //Page<Transaction> findByWallet_TransactionHistory(Wallet wallet, Pageable pageable);

}
