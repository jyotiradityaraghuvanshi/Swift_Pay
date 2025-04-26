package com.swiftpay.SwiftPay.repository;

import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WalletRepository extends JpaRepository<Wallet , Long> {
    boolean existsByUser(User user);

    Optional<Wallet> findByUser(User user);
}
