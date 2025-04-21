package com.swiftpay.SwiftPay.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double balance;

    private Timestamp updatedAt;

}
