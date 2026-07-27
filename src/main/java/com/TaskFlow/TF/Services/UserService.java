package com.TaskFlow.TF.Services;


import com.TaskFlow.TF.DTOs.UserRequests;
import com.TaskFlow.TF.DTOs.UserResponse;
import com.TaskFlow.TF.Models.User;
import com.TaskFlow.TF.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.PublicKey;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("User Not found by this id: "+id ));

    }
    public UserResponse registerUser(UserRequests request){
        User user  = new User();
        user.setUsername(request.getUsername());
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getUsername());
    }
    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("rehaan");
            user.setPassword(passwordEncoder.encode("password123"));
            userRepository.save(user);
        }
    }

}
