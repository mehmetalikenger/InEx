package com.stage.inex;

import com.stage.inex.domain.model.User;
import com.stage.inex.domain.port.PasswordEncoder;
import com.stage.inex.infrastructure.security.PasswordEncoderImp;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordEncoderTests {

    PasswordEncoder passwordEncoder = new PasswordEncoderImp();

    @Test
    @Tag(value = "success")
    public void shouldEncodePasswordSuccessfully(){

        String encodedPassword = passwordEncoder.encode("Zxcv123.4");

        new User.EncodedPassword(encodedPassword);
    }

    @Test
    @Tag(value = "failure")
    public void shouldThrowWhenPasswordsDoNotMatch(){

        assertThrows(RuntimeException.class, () -> {

            passwordEncoder.matches("123", "$2e4rt");
        });
    }

    @Test
    @Tag(value = "success")
    public void shouldMatchPasswordsSuccessfully(){

        passwordEncoder.matches("Zxcv123.4+", "$2a$12$.hIy6v0VP.zTdosJjp1Ot.dI0GTTUgaNwSQN5cs5BE9y838uUoOsK");
    }
}
