package com.stage.inex.application.service;

import com.stage.inex.domain.data.AuthResponseData;
import com.stage.inex.domain.data.UserAuthData;
import com.stage.inex.domain.port.TokenGenerator;
import com.stage.inex.domain.service.UserAuthDomainService;
import com.stage.inex.presentation.dto.UserAuthDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

    private final UserAuthDomainService userAuthDomainService;
    private final TokenGenerator tokenGenerator;

    public UserAuthService(UserAuthDomainService userAuthDomainService, TokenGenerator tokenGenerator){

        this.userAuthDomainService = userAuthDomainService;
        this.tokenGenerator = tokenGenerator;
    }

    public AuthResponseData authenticate(UserAuthDTO dto){

        UserAuthData authData = new UserAuthData(dto.email(), dto.password(), dto.rememberMe());

        return userAuthDomainService.authenticate(authData);
    }

    public AuthResponseData generateAccessToken(){

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        assert authentication != null;
        String email = (String) authentication.getPrincipal();
        Boolean rememberMe = (boolean) authentication.getCredentials();

        return new AuthResponseData(tokenGenerator.generateAccessToken(email),
                tokenGenerator.generateRefreshToken(email, rememberMe));
    }
}
