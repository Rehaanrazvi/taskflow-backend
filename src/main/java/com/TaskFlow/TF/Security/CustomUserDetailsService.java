package com.TaskFlow.TF.Security;

import com.TaskFlow.TF.Models.User;
import com.TaskFlow.TF.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // 1. Register this as a Spring Bean
public class CustomUserDetailsService implements UserDetailsService { // 2. Implement the interface

    @Autowired
    private UserRepository userRepository; // 3. We need the DB

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 4. Fetch user from DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 5. Convert our User to Spring Security's UserDetails object
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // This is the HASED password!
                .roles("USER") // Default role for now
                .build();
    }
}