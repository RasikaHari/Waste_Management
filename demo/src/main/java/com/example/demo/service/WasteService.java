package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.WasteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class WasteService {

    private final WasteRepository wasteRepository;
    private final UserService userService;
    private final FlaskService flaskService;

    public WasteService(WasteRepository wasteRepository,
                        UserService userService,
                        FlaskService flaskService) {
        this.wasteRepository = wasteRepository;
        this.userService = userService;
        this.flaskService = flaskService;
    }

    public Waste postWaste(MultipartFile file,
                           String title,
                           String description,
                           Integer quantity,
                           String email) {

        try {
            User user = userService.findByEmail(email);

            String response = flaskService.sendImage(file.getBytes());
           
           ObjectMapper mapper = new ObjectMapper();

Map<String, Object> result = mapper.readValue(response, Map.class);

String wasteType = (String) result.get("prediction");


if (wasteType == null || wasteType.trim().isEmpty()) {
    wasteType = "UNKNOWN";
}
if ("FOOD".equalsIgnoreCase(wasteType)) {
    if (quantity == null || quantity < 1) {
        throw new RuntimeException("Food waste must have quantity >= 1");
    }
} else {
    quantity = null; 
}
if (title == null) {
    title = "";
}
if (description == null) {
    description = "";
}


          
            String original = Optional.ofNullable(file.getOriginalFilename())
        .orElse("image.jpg");

            String fileName = UUID.randomUUID() + "_" + original.replaceAll("\\s+", "_");
           
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String filePath = uploadDir + fileName;
            file.transferTo(new File(filePath));

          
            Waste waste = new Waste();
            waste.setTitle(title);
            waste.setDescription(description);
            waste.setQuantity(quantity);
            waste.setWasteType(wasteType);
            waste.setImagePath(filePath);
       
            if (wasteType.equalsIgnoreCase("FOOD")) 
            {
                waste.setStatus(WasteStatus.AVAILABLE);
            } else {
                waste.setStatus(WasteStatus.NON_EDIBLE);
            }
            waste.setCreatedAt(LocalDateTime.now());
            waste.setUser(user);

            return wasteRepository.save(waste);

        } catch (Exception e) {
    e.printStackTrace();  
    throw new RuntimeException("Upload failed: " + e.getMessage());
}
    }

    public Waste getWasteById(Long id) {
    return wasteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Waste not found"));
}
    public List<Waste> getUserWaste(String email) {
        User user = userService.findByEmail(email);
        return wasteRepository.findByUser(user);
    }

    public List<Waste> getAvailableWaste() {
        return wasteRepository.findByStatus(WasteStatus.AVAILABLE);
    }

    public Waste acceptWaste(Long wasteId, String email) {

        Waste waste = wasteRepository.findById(wasteId)
                .orElseThrow(() -> new RuntimeException("Waste not found"));

        if (waste.getAcceptedBy() != null) {
            throw new RuntimeException("Already accepted");
        }
         if (waste.getCreatedAt().plusHours(3).isBefore(LocalDateTime.now())) {
        waste.setStatus(WasteStatus.EXPIRED);
        wasteRepository.save(waste);
        throw new RuntimeException("Waste is expired and cannot be accepted");
        }
        if (waste.getStatus() != WasteStatus.AVAILABLE) {
            throw new RuntimeException("Already accepted/expired");
        }

        User orphanage = userService.findByEmail(email);
        if (orphanage.getRole() != Role.ORPHANAGE) {
            throw new RuntimeException("Only orphanage can accept waste");
        }
        waste.setStatus(WasteStatus.ACCEPTED);
        waste.setAcceptedBy(orphanage);

        return wasteRepository.save(waste);
    }

    public void deleteWaste(Long id, String email) {
    Waste waste = wasteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Waste not found"));

    if (!waste.getUser().getEmail().equals(email)) {
        throw new RuntimeException("Unauthorized");
    }

    wasteRepository.delete(waste);
}

    @Scheduled(fixedRate = 60000)
    public void expireOldWaste() {

        List<Waste> list = wasteRepository.findByStatus(WasteStatus.AVAILABLE);

        for (Waste waste : list) {
            if (waste.getCreatedAt().plusHours(3).isBefore(LocalDateTime.now())) {
                waste.setStatus(WasteStatus.EXPIRED);
                wasteRepository.save(waste);
            }
        }
    }
}