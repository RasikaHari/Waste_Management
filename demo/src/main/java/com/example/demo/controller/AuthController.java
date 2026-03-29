package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import com.example.demo.service.*;
import com.example.demo.security.*;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;

    @Autowired
    private TempUserService tempUserService;

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest request){

        User user = userService.findByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return response;
    }

    @PostMapping("/register/init")
    public String sendOtp(@RequestBody RegisterRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
        throw new RuntimeException("Passwords do not match");
        }
        tempUserService.saveTempUser(request);

        otpService.generateAndSendOtp(
                request.getEmail(),
                request.getFullName()
        );

        return "OTP sent to email";
    }
    @PostMapping("/register/verify")
    public User verifyOtp(@RequestBody VerifyOtpRequest request) {

        boolean isValid = otpService.validateAndConsumeOtp(
                request.getEmail(),
                request.getOtp()
        );

        if (!isValid) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        RegisterRequest savedRequest =
                tempUserService.getTempUser(request.getEmail());

        if (savedRequest == null) {
            throw new RuntimeException("No registration data found");
        }

        User user = userService.saveUserAfterOtp(savedRequest);

        tempUserService.removeTempUser(request.getEmail());

        return user;
    }

    @PostMapping("/forgot-password/init")
    public String forgotPasswordInit(@RequestParam String email) {

        User user = userService.findByEmail(email);

        otpService.generateAndSendOtp(email, user.getFullName());

        return "OTP sent to email";
    }

    @PostMapping("/forgot-password/verify")
    public String resetPassword(@RequestBody ForgotPasswordRequest request) {

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        boolean isValid = otpService.validateAndConsumeOtp(
                request.getEmail(),
                request.getOtp()
        );

        if (!isValid) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        userService.updatePassword(
                request.getEmail(),
                request.getNewPassword()
        );

        return "Password updated successfully";
    }
}