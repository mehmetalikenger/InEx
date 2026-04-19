package com.stage.inex.infrastructure.security;
import com.stage.inex.domain.port.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderImp implements PasswordEncoder {

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String encode(CharSequence rawPassword) {

        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
