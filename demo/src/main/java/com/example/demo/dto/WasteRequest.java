package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class WasteRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String foodType;

    @NotNull
    private Integer quantity;
}