package com.stage.inex.presentation.dto;


import jakarta.validation.constraints.*;
import jdk.jfr.BooleanFlag;

public record UserAuthDTO(

        @Email(message = "Email is not valid.")
        @NotBlank(message = "Email cannot be blank.")
        String email,

        @NotBlank(message = "Password cannot be blank.")
        String password,

        @NotNull(message = "Remember me cannot be blank.")
        Boolean rememberMe
) {}
