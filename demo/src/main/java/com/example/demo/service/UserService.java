package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.*;
import com.example.demo.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

  
    public User saveUserAfterOtp(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());

     
        user.setHouseNumber(request.getHouseNumber());
        user.setStreet(request.getStreet());
        user.setArea(request.getArea());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setPincode(request.getPincode());

       
        user.setLatitude(request.getLatitude());
        user.setLongitude(request.getLongitude());

      
        user.setHouseholdSize(request.getHouseholdSize());
        user.setWastePreference(request.getWastePreference());

        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

  
   public void updatePassword(String email, String newPassword) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setPassword(passwordEncoder.encode(newPassword));

    userRepository.save(user);
}

  
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
            if (!userRepository.existsById(id)) {
                throw new RuntimeException("User not found");
            }
    userRepository.deleteById(id);
    }
    
}