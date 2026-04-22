package com.stage.inex.unitTest;

import com.stage.inex.domain.model.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserEntityTests {

    @Test
    @Tag(value = "failure")
    public void shouldThrowWhenPasswordIsNotHashed(){

        assertThrows(RuntimeException.class, () -> {

            new User.EncodedPassword("Zxcv123.4+");
        });
    }

    @Test
    @Tag(value = "success")
    public void shouldValidateHashedPassword(){

        new User.EncodedPassword("$2");
    }
}
