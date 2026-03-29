package com.example.demo.service;


import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.example.demo.model.OtpVerification;
import com.example.demo.repository.OtpVerificationRepository;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpVerificationRepository otpRepo;
    private final EmailService emailService;

    
    @Transactional
    public void generateAndSendOtp(String email, String nameForEmail) {
        String otp = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

       
        Optional<OtpVerification> existingOpt = otpRepo.findByEmail(email);

        OtpVerification entity;
        if (existingOpt.isPresent()) {
            entity = existingOpt.get();
            entity.setOtp(otp);
            entity.setExpiryTime(expiry);
        } else {
            entity = new OtpVerification();
            entity.setEmail(email);
            entity.setOtp(otp);
            entity.setExpiryTime(expiry);
        }

        otpRepo.save(entity); 
        String subject = "Your OTP Code";
        String body = "Hello " + (nameForEmail == null ? "" : nameForEmail) + "\n\n"
                + "Your OTP is: " + otp + "\n"
                + "It is valid for 5 minutes.";
        emailService.sendEmail(email, subject, body);
    }

 
    @Transactional
    public boolean validateAndConsumeOtp(String email, String otp) {
        Optional<OtpVerification> rec = otpRepo.findByEmail(email);
        if (rec.isEmpty()) return false;

        OtpVerification entity = rec.get();
        boolean notExpired = entity.getExpiryTime().isAfter(LocalDateTime.now());
        boolean matches = entity.getOtp().equals(otp);

        if (matches && notExpired) {
            otpRepo.delete(entity); 
            return true;
        }
        return false;
    }

    
    @Transactional
    public void resendOtp(String email, String nameForEmail) {
        generateAndSendOtp(email, nameForEmail);
    }
}
