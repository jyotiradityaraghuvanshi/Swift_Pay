package com.swiftpay.SwiftPay.services;


import com.swiftpay.SwiftPay.entity.User;
import com.swiftpay.SwiftPay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user){
        return userRepository.save(user);
    }


    public User getUser(Long id){
        return userRepository.findById(id).orElse(null);
    }


    public User checkUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
