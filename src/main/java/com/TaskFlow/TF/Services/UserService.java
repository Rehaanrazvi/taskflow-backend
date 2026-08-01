package com.TaskFlow.TF.Services;


import com.TaskFlow.TF.DTOs.UserRequests;
import com.TaskFlow.TF.DTOs.UserResponse;
import com.TaskFlow.TF.Exceptions.ResourceAlreadyExistsException;
import com.TaskFlow.TF.Exceptions.ResourceNotFoundException;
import com.TaskFlow.TF.Models.User;
import com.TaskFlow.TF.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User",id));

    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
    }
    public UserResponse registerUser(UserRequests request){
        User user  = new User();
        String usName = request.getUsername();
        if(userRepository.findByUsername(usName).isPresent()){
            throw new ResourceAlreadyExistsException("Username",usName);
        }
        user.setUsername(usName);
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        user.setRole("USER");
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getUsername());
    }
    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("rehaan");
            user.setPassword(passwordEncoder.encode("password123"));
            user.setRole("USER");
            userRepository.save(user);

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }
    }

}
