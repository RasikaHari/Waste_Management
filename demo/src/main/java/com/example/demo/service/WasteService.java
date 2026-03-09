package com.example.demo.service;

import com.example.demo.dto.WasteRequest;
import com.example.demo.model.*;
import com.example.demo.repository.WasteRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WasteService {

    private final WasteRepository wasteRepository;
    private final UserService userService;

    public WasteService(WasteRepository wasteRepository,
                        UserService userService) {
        this.wasteRepository = wasteRepository;
        this.userService = userService;
    }

    // 👤 USER posts waste
   public Waste postWaste(WasteRequest request, String email) {

    User user = userService.findByEmail(email);

    Waste waste = new Waste();
    waste.setTitle(request.getTitle());
    waste.setDescription(request.getDescription());
    waste.setFoodType(request.getFoodType());
    waste.setQuantity(request.getQuantity());
    waste.setStatus(WasteStatus.AVAILABLE);
    waste.setCreatedAt(LocalDateTime.now());
    waste.setUser(user);

    return wasteRepository.save(waste);
}

    // 👤 USER views own waste
    public List<Waste> getUserWaste(String email) {
        User user = userService.findByEmail(email);
        return wasteRepository.findByUser(user);
    }

    // 🏢 ORPHANAGE views available waste
    public List<Waste> getAvailableWaste() {
        return wasteRepository.findByStatus(WasteStatus.AVAILABLE);
    }

    // 🏢 ORPHANAGE accepts waste
   public Waste acceptWaste(Long wasteId, String email) {

    Waste waste = wasteRepository.findById(wasteId)
            .orElseThrow(() -> new RuntimeException("Waste not found"));

    if (waste.getStatus() != WasteStatus.AVAILABLE) {
        throw new RuntimeException("Waste already accepted or expired");
    }

    User orphanage = userService.findByEmail(email);

    waste.setStatus(WasteStatus.ACCEPTED);
    waste.setAcceptedBy(orphanage);

    return wasteRepository.save(waste);
}
    // 👤 USER delete waste
    public void deleteWaste(Long id) {
        wasteRepository.deleteById(id);
    }
        @Scheduled(fixedRate = 60000) // every 1 minute
public void expireOldWaste() {

    List<Waste> availableWaste = wasteRepository.findByStatus(WasteStatus.AVAILABLE);

    for (Waste waste : availableWaste) {

        if (waste.getCreatedAt().plusHours(3).isBefore(LocalDateTime.now())) {

            waste.setStatus(WasteStatus.EXPIRED);
            wasteRepository.save(waste);
        }
    }
}
}