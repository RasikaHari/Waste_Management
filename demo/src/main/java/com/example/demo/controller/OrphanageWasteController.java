package com.example.demo.controller;

import com.example.demo.model.Waste;
import com.example.demo.service.WasteService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orphanage/waste")
public class OrphanageWasteController {

    private final WasteService wasteService;

    public OrphanageWasteController(WasteService wasteService) {
        this.wasteService = wasteService;
    }

    // 🏢 View Available Waste
    @GetMapping
    public List<Waste> getAvailableWaste() {
        return wasteService.getAvailableWaste();
    }

    // 🏢 Accept Waste
    
    @PutMapping("/{id}/accept")
public Waste acceptWaste(@PathVariable Long id,
                         Authentication authentication) {

    String email = authentication.getName();

    return wasteService.acceptWaste(id, email);
}
}