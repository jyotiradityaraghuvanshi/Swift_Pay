package com.swiftpay.SwiftPay.entity;

import com.swiftpay.SwiftPay.enums.Role;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // default Role for everyOne

    public User(long l, String test, String mail) {
    }
}
