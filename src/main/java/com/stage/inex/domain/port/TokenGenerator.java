package com.stage.inex.domain.port;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenGenerator {

    public String generateAccessToken(String email);
    public String generateRefreshToken(String email, Boolean rememberMe);
}
