package com.stage.inex.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final String secretKeyString;

    public JwtFilter(@Value("${spring.security.jwt.secret-key}") String secretKeyString){

        this.secretKeyString = secretKeyString;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null){

            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authHeader.substring(7);

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));

        Jwt<?,?> jwt;

        try{

            jwt = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken);

        } catch (JwtException ex) {

            throw new ServletException();
        }

        Claims claims = (Claims) jwt.getPayload();
        String email = claims.getSubject();

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(email, null, null);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }
}
