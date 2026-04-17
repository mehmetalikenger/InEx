package com.stage.inex.application.service;

import com.stage.inex.domain.data.UserRegistrationData;
import com.stage.inex.domain.service.UserRegistrationDomainService;
import com.stage.inex.presentation.dto.UserRegistrationDTO;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    UserRegistrationDomainService userRegistrationDomainService;

    public UserRegistrationService(UserRegistrationDomainService userRegistrationDomainService){

        this.userRegistrationDomainService = userRegistrationDomainService;
    }

    public void register(UserRegistrationDTO dto){

        UserRegistrationData data = new UserRegistrationData(dto.name(), dto.surname(), dto.email(), dto.password());

        userRegistrationDomainService.registerUser(data);
    }
}
