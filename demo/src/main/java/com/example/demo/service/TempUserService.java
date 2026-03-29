package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TempUserService {

    private final Map<String, RegisterRequest> tempUsers = new HashMap<>();

    public void saveTempUser(RegisterRequest request) {
        tempUsers.put(request.getEmail(), request);
    }

    
    public RegisterRequest getTempUser(String email) {
        return tempUsers.get(email);
    }

  
    public void removeTempUser(String email) {
        tempUsers.remove(email);
    }
}