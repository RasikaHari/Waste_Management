package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.service.FlaskService;

@RestController
@RequestMapping("/api")
public class PredictionController {

    @Autowired
    private FlaskService flaskService;

    @PostMapping("/predict")
    public String predict(@RequestParam("file") MultipartFile file) {
        try {
            return flaskService.sendImage(file.getBytes());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}