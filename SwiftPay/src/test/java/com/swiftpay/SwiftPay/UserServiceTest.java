package com.swiftpay.SwiftPay;

import com.swiftpay.SwiftPay.dto.RequestDto.UserRequestDto;
import com.swiftpay.SwiftPay.dto.ResponseDto.UserResponseDto;
import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.repository.UserRepository;
import com.swiftpay.SwiftPay.services.MailService;
import com.swiftpay.SwiftPay.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // this enables the mockito in our test class
public class UserServiceTest {

    @InjectMocks // this injects the mock service for test purpose provided by mockito
    private UserService userService;

    @Mock // this makes a mock instance for the particular class
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    @Test // this annotation tells spring that this particular method is a test
    void testFindByEmailSuccess(){
        User user = new User();
        user.setEmail("yashraghuvanshi1108@gmail.com");

        // Mocking the call if necessary.
        when(userRepository.findByEmail("yashraghuvanshi1108@gmail.com")).thenReturn(Optional.of(user));
        User actual = userService.ensureUserExist("yashraghuvanshi1108@gmail.com"); // calling actual method .

        Assertions.assertEquals(user , actual);
        
    }

    @Test
    void testCreateUser_UserCreationSuccessful(){

        UserRequestDto user = new UserRequestDto();

        user.setEmail("test1@gmail.com");
        user.setName("Test name");
        user.setPassword("123456");

        User temp = new User();

        temp.setName("Test name");
        temp.setEmail("test1@gmail.com");
        temp.setPassword("encodedPwd");

        UserResponseDto expectedResponse = new UserResponseDto();
        expectedResponse.setName("Test name");
        expectedResponse.setEmail("test1@gmail.com");

        Mockito.when(passwordEncoder.encode("123456")).thenReturn("encodedPwd");
        Mockito.when(userRepository.save(any(User.class))).thenReturn(temp); // mocking the call not to save in DB.
        Mockito.doNothing().when(mailService).sendEmail(anyString() , anyString() , anyString() , anyMap());
        UserResponseDto savedResponse = userService.createUser(user);

        // Assertions to check.
        Assertions.assertEquals(expectedResponse.getEmail(), savedResponse.getEmail());
        Assertions.assertEquals(expectedResponse.getName(), savedResponse.getName());

    }

}
