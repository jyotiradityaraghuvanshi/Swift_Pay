package com.swiftpay.SwiftPay.dto.ResponseDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDto {

    public Long transactionId;

    public Long senderWalletId;

    public Long receiverWalletId;

    public Double amount;

    public Timestamp transactionTime;

}
