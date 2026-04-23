package com.stage.inex.infrastructure.security;

import com.stage.inex.domain.port.PasswordValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidatorImp implements PasswordValidator {

    public void validate(String rawPassword){

        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#%^&*._+])(?!.*\\$).{8,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rawPassword);

        if(!matcher.matches()){
            throw new IllegalArgumentException("Password should be minimum 8 characters long. " +
                    "Password should contain at least one lowercase, " +
                    "one uppercase, one number and one special character. " +
                    "Allowed special characters are (!@#%^&*._+)");
        }
    }
}
