package com.stage.inex.infrastructure.security;
import com.stage.inex.domain.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderImp implements PasswordEncoder {

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String encode(CharSequence password) {

        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }
}
