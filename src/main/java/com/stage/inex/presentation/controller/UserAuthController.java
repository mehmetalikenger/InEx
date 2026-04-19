package com.stage.inex.presentation.controller;

import com.stage.inex.application.service.UserAuthService;
import com.stage.inex.domain.data.AuthResponseData;
import com.stage.inex.presentation.dto.UserAuthDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserAuthController {

    private final UserAuthService userAuthService;

    private final String sameSite;

    private final int accessTokenMaxAge;

    private final int refreshTokenMaxAge;

    public UserAuthController(UserAuthService userAuthService,
                              @Value("${spring.security.jwt.same-site}") String sameSite,
                                @Value("${spring.security.jwt.access-token-exp-time}") int accessTokenMaxAge,
                              @Value("${spring.security.jwt.refresh-token-exp-time}") int refreshTokenMaxAge){

        this.userAuthService = userAuthService;
        this.sameSite = sameSite;
        this.accessTokenMaxAge = accessTokenMaxAge;
        this.refreshTokenMaxAge = refreshTokenMaxAge;
    }

    @PostMapping("/auth")
    public ResponseEntity<Void> auth(@RequestBody @Valid UserAuthDTO dto){

        AuthResponseData responseData = userAuthService.authenticate(dto);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", responseData.accessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite(sameSite)
                .path("/")
                .maxAge(accessTokenMaxAge)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", responseData.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite(sameSite)
                .path("/")
                .maxAge(refreshTokenMaxAge)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }
}
