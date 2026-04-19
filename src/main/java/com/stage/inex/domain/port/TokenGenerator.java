package com.stage.inex.domain.port;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenGenerator {

    public String generateAccessToken(UserDetails userDetails);
    public String generateRefreshToken(UserDetails userDetails);
}
