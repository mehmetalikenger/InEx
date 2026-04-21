package com.stage.inex.infrastructure.security;
import com.stage.inex.domain.exception.PasswordsDoNotMatchException;
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
    public void matches(String rawPassword, String encodedPassword) {

        bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if(!bCryptPasswordEncoder.matches(rawPassword, encodedPassword)){

            throw new PasswordsDoNotMatchException("Passwords do not match.");
        }
    }
}
