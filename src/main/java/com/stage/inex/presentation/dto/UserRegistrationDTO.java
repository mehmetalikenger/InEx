package com.stage.inex.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegistrationDTO(

        @NotBlank(message = "Name cannot be blank.")
        String name,

        String surname,

        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Email is not valid.")
        String email,

        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 8, message = "Password should be minimum 8 characters long.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#%^&*._+])(?!.*\\$).{8,}$", message = "Password should contain at least one lowercase, " +
                "one uppercase, one number and one special character. " +
                "Allowed special characters are (!@#%^&*._+)")
        String password
) {}
