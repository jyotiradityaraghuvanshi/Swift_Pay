package com.swiftpay.SwiftPay.dto.RequestDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestDto {

    public Long senderWalletId;

    public Long receiverWalletId;

    public double amount;

}
