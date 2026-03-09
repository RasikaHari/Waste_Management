package com.example.demo.model;

import jakarta.persistence.*;
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

    private String foodType;
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private WasteStatus status;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Posted by USER
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Accepted by ORPHANAGE
    @ManyToOne
    @JoinColumn(name = "accepted_by")
    private User acceptedBy;
}