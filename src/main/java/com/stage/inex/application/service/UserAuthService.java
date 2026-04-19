package com.stage.inex.application.service;

import com.stage.inex.domain.data.AuthResponseData;
import com.stage.inex.domain.data.UserAuthData;
import com.stage.inex.domain.service.UserAuthDomainService;
import com.stage.inex.presentation.dto.UserAuthDTO;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

    private final UserAuthDomainService userAuthDomainService;

    public UserAuthService(UserAuthDomainService userAuthDomainService){

        this.userAuthDomainService = userAuthDomainService;
    }

    public AuthResponseData authenticate(UserAuthDTO dto){

        UserAuthData authData = new UserAuthData(dto.email(), dto.password());

        return userAuthDomainService.authenticate(authData);
    }
}
