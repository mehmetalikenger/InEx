package com.stage.inex.unitTest;
import com.stage.inex.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class JwtServiceTests {

    JwtService jwtService;

    @BeforeEach
    void setup(){
        jwtService = new JwtService("dGVzdFNlY3JldEtleUZvckpXVFRlc3RpbmdQdXJwb3Nlcw==");
        jwtService.init();
    }

    @Test
    @Tag(value = "success")
    void shouldGenerateJwtSuccessfully(){

        String token = jwtService.generateToken("test@email.com", 3600);

        Pattern pattern = Pattern.compile("^ey.*\\.ey.*\\..*$");
        Matcher matcher = pattern.matcher(token);

        assertTrue(matcher.matches());
    }
}
