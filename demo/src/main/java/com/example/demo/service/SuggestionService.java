package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.*;

import java.io.OutputStream;

@Service
public class SuggestionService {


    @Value("${youtube.api.key}")
    private String YOUTUBE_API_KEY;
     private static final Map<String, List<String>> SUGGESTION_MAP = new HashMap<>();
    static {
        SUGGESTION_MAP.put("ewaste", Arrays.asList(
                "Donate old smartphones or laptops to NGOs or schools for educational purposes.",
                "Repurpose old phone chargers and cables for electronics DIY projects.",
                "Separate metals, plastics, and circuit boards before recycling to ensure proper e-waste disposal."
        ));
        SUGGESTION_MAP.put("food_waste", Arrays.asList(
                "Turn vegetable peels and leftover food into compost to enrich your garden soil.",
                "Freeze extra food portions instead of throwing them away to reduce waste.",
                "Use stale bread to make breadcrumbs or feed backyard chickens."
        ));
        SUGGESTION_MAP.put("leaf_waste", Arrays.asList(
                "Use dry leaves as mulch around plants to retain moisture and reduce weeds.",
                "Shred leaves to create nutrient-rich compost for your garden.",
                "Make decorative leaf garlands or DIY paper crafts from fallen leaves."
        ));
        SUGGESTION_MAP.put("metal_cans", Arrays.asList(
                "Convert empty cans into pen holders, mini planters, or candle holders.",
                "Donate clean metal cans to local art or DIY workshops for creative projects.",
                "Crush and recycle aluminum cans to reduce landfill space."
        ));
        SUGGESTION_MAP.put("paper_waste", Arrays.asList(
                "Reuse old notebooks or scrap paper for jotting notes or making origami.",
                "Shred paper to use as packing material instead of plastic bubble wrap.",
                "Recycle newspapers or magazines to create handmade envelopes or gift wraps."
        ));
        SUGGESTION_MAP.put("plastic_bags", Arrays.asList(
                "Weave or knot plastic bags to create reusable shopping bags or rugs.",
                "Use bags as liners for trash bins to avoid dirtying the bins.",
                "Donate clean plastic bags to local stores that collect them for recycling."
        ));
        SUGGESTION_MAP.put("plastic_bottles", Arrays.asList(
                "Cut bottles to make mini planters or self-watering plant containers.",
                "Create bird feeders or storage boxes using old bottles.",
                "Use bottles in DIY projects like vertical gardens or decorative lights."
        ));
        SUGGESTION_MAP.put("wood_waste", Arrays.asList(
                "Repurpose wood scraps to make shelves, coasters, or small furniture.",
                "Donate usable wood pieces to local schools or carpenters for craft projects.",
                "Turn small untreated wood pieces into mulch for your garden paths."
        ));
        
    }
public List<String> getSuggestions(String wasteType) {
  
    return SUGGESTION_MAP.getOrDefault(wasteType.toLowerCase(), 
                                       Collections.singletonList("No suggestions available"));
}


private static final Map<String, String> REASONING_MAP = new HashMap<>();
static {
    REASONING_MAP.put("ewaste", "Donating or recycling e-waste prevents toxic materials from polluting the environment.");
    REASONING_MAP.put("food_waste", "Composting food waste enriches soil and reduces landfill methane.");
    REASONING_MAP.put("leaf_waste", "Shredding leaves or using them as mulch nourishes your garden naturally.");
    REASONING_MAP.put("metal_cans", "Recycling metal saves energy and reduces mining demand.");
    REASONING_MAP.put("paper_waste", "Reusing or recycling paper saves trees and water.");
    REASONING_MAP.put("plastic_bottles", "Repurposing plastic reduces pollution and landfill waste.");
    REASONING_MAP.put("plastic_bags", "Reusing plastic bags reduces single-use plastic pollution.");
    REASONING_MAP.put("wood_waste", "Repurposing wood prevents deforestation and promotes DIY projects.");
}

public String getReasoning(String wasteType) {
    return REASONING_MAP.getOrDefault(wasteType.toLowerCase(), "No reasoning available");
}
  
    public List<String> getYoutubeLinks(String wasteType) {

        List<String> videos = new ArrayList<>();

        try {
            String query = "how to recycle " + wasteType;

            String urlStr = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="
                    + URLEncoder.encode(query, "UTF-8")
                    + "&key=" + YOUTUBE_API_KEY
                    + "&maxResults=3";

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Scanner sc = new Scanner(conn.getInputStream());
            String response = sc.useDelimiter("\\A").next();
            sc.close();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(response, Map.class);

            List<Map<String, Object>> items = (List<Map<String, Object>>) map.get("items");

            for (Map<String, Object> item : items) {
                Map<String, Object> id = (Map<String, Object>) item.get("id");
                if (id.get("videoId") != null) {
                    String videoId = (String) id.get("videoId");
                    videos.add("https://www.youtube.com/watch?v=" + videoId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            videos.add("Error fetching videos");
        }

        return videos;
    }
}