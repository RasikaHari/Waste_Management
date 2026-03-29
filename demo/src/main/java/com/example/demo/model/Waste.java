package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Waste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    
    private String wasteType;

    @Min(1)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private WasteStatus status;

    private LocalDateTime createdAt = LocalDateTime.now();

   
    private String imagePath;

    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    
    @ManyToOne
    @JoinColumn(name = "accepted_by")
    private User acceptedBy;
}