package com.stage.inex.presentation.dto;


import jakarta.validation.constraints.*;

public record UserAuthDTO(

        @NotBlank(message = "Email can't be blank.")
        @Email
        String email,

        @NotBlank(message = "Password can't be blank.")
        String password
) {}
