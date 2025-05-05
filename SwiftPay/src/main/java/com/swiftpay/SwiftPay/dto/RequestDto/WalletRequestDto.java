package com.swiftpay.SwiftPay.dto.RequestDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequestDto {

    public Long userId;

    public Double balance;

}
