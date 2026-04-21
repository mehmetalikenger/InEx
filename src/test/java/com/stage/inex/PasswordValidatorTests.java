package com.stage.inex;

import com.stage.inex.infrastructure.security.PasswordValidatorImp;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordValidatorTests {

    PasswordValidatorImp passwordValidator = new PasswordValidatorImp();

    @Test
    @Tag(value = "failure")
    public void shouldThrowExceptionWhenPasswordIsShort(){

        assertThrows(RuntimeException.class, () -> {

            passwordValidator.validate("Za1.");
        });
    }

    @Test
    @Tag(value = "failure")
    public void shouldThrowWhenPasswordDoesNotHaveUppercaseLetter(){

        assertThrows(RuntimeException.class, () -> {

            passwordValidator.validate("za1.za1.");
        });
    }

    @Test
    @Tag(value = "failure")
    public void shouldThrowWhenPasswordDoesNotHaveLowercaseLetter(){

        assertThrows(RuntimeException.class, () -> {

            passwordValidator.validate("ZA1.ZA1.");
        });
    }

    @Test
    @Tag(value = "failure")
    public void shouldThrowWhenPasswordDoesNotHaveNumber(){

        assertThrows(RuntimeException.class, () -> {

            passwordValidator.validate("zae.zae.");
        });
    }


    @Test
    @Tag(value = "failure")
    public void shouldThrowWhenPasswordDoesNotHaveSpecialChar(){

        assertThrows(RuntimeException.class, () -> {

            passwordValidator.validate("Zaezae123");
        });
    }

    @Test
    @Tag(value = "failure")
    public void shouldThrowWhenPasswordHaveInvalidSpecialChar(){

        assertThrows(RuntimeException.class, () -> {

            passwordValidator.validate("Zaezae123.$");
        });
    }

    @Test
    @Tag(value = "success")
    public void shouldValidatePasswordSuccessfully(){

        passwordValidator.validate("Zaezae123+");
    }
}
