package com.example.demo.dto;
import lombok.*;

@Getter
@Setter
public class ForgotPasswordRequest {
    private String email;
    private String newPassword;
    private String confirmPassword;
    private String otp;
}