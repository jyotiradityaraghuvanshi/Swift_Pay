package com.swiftpay.SwiftPay.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    public String name;

    public String email;

    public String phoneNumber;

    public Timestamp createdAt;

}
