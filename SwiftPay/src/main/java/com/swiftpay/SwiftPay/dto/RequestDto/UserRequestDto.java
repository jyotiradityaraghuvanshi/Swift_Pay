package com.swiftpay.SwiftPay.dto.RequestDto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Name is required")
    public String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is Invalid")
    public String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6 , message = "Password must be at least 6 characters long")
    public String password;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10 , max = 15 , message = "Phone number must be between 10 and 15 digits")
    public String phoneNumber;

}
