package com.swiftpay.SwiftPay.repository;

import com.swiftpay.SwiftPay.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpVerification , Long> {

    Optional<OtpVerification> findByEmail(String email);

}
