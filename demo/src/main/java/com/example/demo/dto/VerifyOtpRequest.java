package com.example.demo.dto;
import lombok.*;
@Getter
@Setter
public class VerifyOtpRequest {
    private String email;
    private String otp;
}