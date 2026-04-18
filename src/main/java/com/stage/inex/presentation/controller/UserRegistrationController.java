package com.stage.inex.presentation.controller;

import com.stage.inex.application.service.UserRegistrationService;
import com.stage.inex.presentation.dto.UserRegistrationDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    public UserRegistrationController(UserRegistrationService userRegistrationService){

        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping("/user")
    public ResponseEntity<Void> register(@RequestBody @Valid UserRegistrationDTO dto){

        userRegistrationService.register(dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
