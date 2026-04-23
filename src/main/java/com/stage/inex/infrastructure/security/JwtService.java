package com.stage.inex.infrastructure.security;

import com.stage.inex.domain.port.TokenGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService implements TokenGenerator {

    private final String secretKeyString;

    @Value("${spring.security.jwt.access-token-exp-time}")
    private String accessTokenExpTime;

    @Value("${spring.security.jwt.refresh-token-exp-time}")
    private String refreshTokenExpTime;

    private SecretKey secretKey;

    public JwtService(@Value("${spring.security.jwt.secret-key}") String secretKeyString){
        this.secretKeyString = secretKeyString;
    }

    @PostConstruct
    public void init(){

        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    @Override
    public String generateAccessToken(String email) {
        return  generateToken(email, Integer.parseInt(accessTokenExpTime));
    }

    @Override
    public String generateRefreshToken(String email) {
        return generateToken(email, Integer.parseInt(refreshTokenExpTime));
    }

    public String generateToken(String email, int expSeconds){

        Instant now = Instant.now();

        return Jwts
                .builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expSeconds)))
                .signWith(secretKey)
                .compact();
    }
}
