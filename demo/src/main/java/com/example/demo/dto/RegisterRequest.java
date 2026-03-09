// package com.example.demo.dto;

// import com.example.demo.model.Role;
// import jakarta.validation.constraints.*;
// import lombok.*;

// @Getter
// @Setter
// public class RegisterRequest {

//     @NotBlank
//     private String fullName;

//     @Email
//     @NotBlank
//     private String email;

//     @Size(min = 6)
//     private String password;

//     @NotBlank
//     private String phoneNumber;

//     @NotNull
//     private Role role;
    
// }
package com.example.demo.dto;

import com.example.demo.model.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    // Basic Details
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Role is required")
    private Role role;

    // Address Details
    @NotBlank(message = "House number is required")
    private String houseNumber;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "Area is required")
    private String area;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pincode is required")
    private String pincode;

    // Location
    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    // USER specific fields
    private Integer householdSize;

    private String wastePreference;
}