package com.stage.inex.infrastructure.security;

import com.stage.inex.domain.port.TokenGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService implements TokenGenerator {

    @Value("${spring.security.jwt.secret-key}")
    private String secretKeyString;

    @Value("${spring.security.jwt.access-token-exp-time}")
    private String accessTokenExpTime;

    @Value("${spring.security.jwt.refresh-token-exp-time}")
    private String refreshTokenExpTime;

    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return  generateToken(userDetails, Integer.parseInt(accessTokenExpTime));
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, Integer.parseInt(refreshTokenExpTime));
    }

    public String generateToken(UserDetails userDetails, int expSeconds){

        Instant now = Instant.now();

        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expSeconds)))
                .signWith(secretKey)
                .compact();
    }
}
