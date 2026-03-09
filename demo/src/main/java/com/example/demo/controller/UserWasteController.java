package com.example.demo.controller;

import com.example.demo.dto.WasteRequest;
import com.example.demo.model.Waste;
import com.example.demo.service.WasteService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/waste")
public class UserWasteController {

    private final WasteService wasteService;

    public UserWasteController(WasteService wasteService) {
        this.wasteService = wasteService;
    }

    // 👤 Post Waste
   @PostMapping
public Waste postWaste(@Valid @RequestBody WasteRequest request,
                       Authentication authentication) {

    return wasteService.postWaste(request, authentication.getName());
}

    // 👤 View My Waste
    @GetMapping
    public List<Waste> getMyWaste(Authentication authentication) {

        String email = authentication.getName();
        return wasteService.getUserWaste(email);
    }

    // 👤 Delete Waste
    @DeleteMapping("/{id}")
    public String deleteWaste(@PathVariable Long id) {
        wasteService.deleteWaste(id);
        return "Waste deleted successfully";
    }
}