package com.example.demo.controller;

import com.example.demo.model.Waste;
import com.example.demo.service.SuggestionService;
import com.example.demo.service.WasteService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/waste")
@CrossOrigin("*")
public class UserWasteController {

    private final WasteService wasteService;
    private final SuggestionService suggestionService;

    public UserWasteController(WasteService wasteService,
                           SuggestionService suggestionService) {
    this.wasteService = wasteService;
    this.suggestionService = suggestionService;
}

   @GetMapping("/{id}/suggestions")
public Map<String, Object> getSuggestions(@PathVariable Long id) {
    Waste waste = wasteService.getWasteById(id);
    Map<String, Object> response = new HashMap<>();

    if (!"FOOD".equalsIgnoreCase(waste.getWasteType())) {
        response.put("wasteType", waste.getWasteType());
        response.put("reasoning", suggestionService.getReasoning(waste.getWasteType()));
        response.put("suggestions", suggestionService.getSuggestions(waste.getWasteType()));
        response.put("videos", suggestionService.getYoutubeLinks(waste.getWasteType()));
    } else {
        response.put("message", "Suggestions not needed for edible waste");
    }

    return response;
}
    @PostMapping("/upload")
    public Waste uploadWaste(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value="title", required = false) String title,
            @RequestParam(value="description", required = false) String description,
            @RequestParam(value="quantity",required = false) Integer quantity,
            Authentication auth) {

        return wasteService.postWaste(
                file,
                title,
                description,
                quantity,
                auth.getName()
        );
    }

    @GetMapping
    public List<Waste> getMyWaste(Authentication auth) {
        return wasteService.getUserWaste(auth.getName());
    }

  
    @DeleteMapping("/{id}")
    public String deleteWaste(@PathVariable Long id, Authentication auth) {
        wasteService.deleteWaste(id, auth.getName());
        return "Deleted";
    }
}