package com.stage.inex.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegistrationDTO(

        @NotBlank(message = "Name cannot be blank.")
        String name,

        String surname,

        @Email(message = "Email is not valid.")
        @NotBlank(message = "Email cannot be blank.")
        String email,

        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#%^&*._+])(?!.*\\$).{8,}$", message = "Password should be minimum 8 characters long. " +
                "Password should contain at least one lowercase, " +
                "one uppercase, one number and one special character. " +
                "Allowed special characters are (!@#%^&*._+)")
        String password
) {}