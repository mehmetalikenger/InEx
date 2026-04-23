package com.stage.inex.presentation.dto;


import jakarta.validation.constraints.*;

public record UserAuthDTO(

        @Email(message = "Email is not valid.")
        @NotBlank(message = "Email cannot be blank.")
        String email,

        @NotBlank(message = "Password cannot be blank.")
        String password
) {}
