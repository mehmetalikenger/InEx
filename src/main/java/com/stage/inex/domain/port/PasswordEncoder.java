package com.stage.inex.domain.port;

public interface PasswordEncoder {

    public String encode(CharSequence rawPassword);
    public void matches(String rawPassword, String encodedPassword);
}
