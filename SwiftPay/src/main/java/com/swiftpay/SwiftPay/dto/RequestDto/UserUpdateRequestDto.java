package com.swiftpay.SwiftPay.dto.RequestDto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDto {

    public Optional<String> name;

    @Email(message = "Email format is Invalid")
    public Optional<String> email;

    @Size(min = 6 , max = 15 , message = "Enter a valid phone number")
    public Optional<String> phoneNumber;

}
