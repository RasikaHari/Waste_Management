package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.*;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Register User
    
    public User registerUser(RegisterRequest request) {

    if (userRepository.existsByEmail(request.getEmail())) {
        throw new RuntimeException("Email already exists!");
    }

    User user = new User();

    // Basic Details
    user.setFullName(request.getFullName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setPhoneNumber(request.getPhoneNumber());
    user.setRole(request.getRole());

    // Address
    user.setHouseNumber(request.getHouseNumber());
    user.setStreet(request.getStreet());
    user.setArea(request.getArea());
    user.setCity(request.getCity());
    user.setState(request.getState());
    user.setPincode(request.getPincode());

    // Location
    user.setLatitude(request.getLatitude());
    user.setLongitude(request.getLongitude());

    // User specific
    user.setHouseholdSize(request.getHouseholdSize());
    user.setWastePreference(request.getWastePreference());

    user.setCreatedAt(LocalDateTime.now());

    return userRepository.save(user);
}

    // Get All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get User By ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findByEmail(String email) {
    return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
}
    // Delete User
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}