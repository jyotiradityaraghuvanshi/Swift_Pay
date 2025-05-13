package com.swiftpay.SwiftPay.services;


import com.swiftpay.SwiftPay.Exception.EmailAlreadyExistException;
import com.swiftpay.SwiftPay.Exception.PasswordMismatchException;
import com.swiftpay.SwiftPay.Exception.UserNotFoundException;
import com.swiftpay.SwiftPay.dto.RequestDto.UserLoginRequestDto;
import com.swiftpay.SwiftPay.dto.RequestDto.UserRequestDto;
import com.swiftpay.SwiftPay.dto.RequestDto.UserUpdateRequestDto;
import com.swiftpay.SwiftPay.dto.ResponseDto.UserResponseDto;
import com.swiftpay.SwiftPay.entity.RefreshToken;
import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.repository.UserRepository;
import com.swiftpay.SwiftPay.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WalletService walletService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public UserResponseDto createUser(UserRequestDto userRequestDto){

        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            throw new EmailAlreadyExistException("Email already exists: " + userRequestDto.getEmail());
        }

        User user = convertDtoToEntity(userRequestDto);

        userRepository.save(user);


        return convertEntityToDto(user);
    }


    public UserResponseDto getUser(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new UserNotFoundException("User with id " + id);
        }

        return convertEntityToDto(user.get());
    }


    public User getUserForServices(Long userId){
        return userRepository.findById(userId).orElseThrow(()->{throw new UserNotFoundException("User Not found for this userId" + userId);});
    }

    public List<UserResponseDto> getAllUser() {
        List<User> userList = userRepository.findAll();
        return userList
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    public Map<String , String> loginUser(UserLoginRequestDto userLoginRequestDto) {
        Optional<User> userOptional = userRepository.findByEmail(userLoginRequestDto.getEmail());
        if(userOptional.isEmpty()){
            throw new UserNotFoundException("User with email " + userLoginRequestDto.getEmail() + " does not found");
        }

        User user = userOptional.get();
        String enteredPassword = userLoginRequestDto.getPassword().trim();
        String storedPassword = user.getPassword().trim();

        // always remember while comparing stored password of DB to given password by user do not compare them directly
        // because the stored password in DB is hashed and given passcode is simple one, so we cannot directly compare these password
        // directly use password encoder matches method to compare this different style passwords*/
        if (!passwordEncoder.matches(enteredPassword, storedPassword)) {
            throw new PasswordMismatchException("Incorrect Password");
        }

        String accessToken = JwtUtil.generateToken(user.getEmail() , user.getRole());
        RefreshToken refreshToken = refreshTokenService.replaceRefreshToken(user);

        Map<String , String> map = new HashMap<>();
        map.put("accessToken" , accessToken);
        map.put("refreshToken" , refreshToken.getToken());

        return map;
    }

    public String getUserEmailById(Long id){

        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException("User does not exist with user id " + id);
        }

        return userOptional
                .get()
                .getEmail();
    }


    public UserResponseDto getUserByEmails(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()) throw new UserNotFoundException("User not found .");

        return convertEntityToDto(optionalUser.get());
    }


    public UserUpdateRequestDto updateUserProfile(Long userId, UserUpdateRequestDto userUpdateRequestDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User Not found for id " + userId);
        }

        User user = optionalUser.get();
        if(userUpdateRequestDto.getEmail().isPresent()) user.setEmail(String.valueOf(userUpdateRequestDto.getEmail()));
        if(userUpdateRequestDto.getName().isPresent()) user.setName(String.valueOf(userUpdateRequestDto.getName()));
        if (userUpdateRequestDto.getPhoneNumber().isPresent()) user.setPhoneNumber(String.valueOf(userUpdateRequestDto.getPhoneNumber()));

        userRepository.save(user);
        return userUpdateRequestDto;
    }

    private UserResponseDto convertEntityToDto(User user){
        return new UserResponseDto(
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCreatedAt()
        );
    }

    private User convertDtoToEntity(UserRequestDto userRequestDto){

        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return user;
    }



}
