package com.stage.inex.domain.port;

public interface PasswordEncoder {

    public String encode(CharSequence rawPassword);
    public boolean matches(String rawPassword, String encodedPassword);
}
