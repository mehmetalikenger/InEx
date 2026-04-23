package com.stage.inex.unitTest;

import com.stage.inex.domain.data.UserRegistrationData;
import com.stage.inex.domain.model.User;
import com.stage.inex.domain.port.PasswordEncoder;
import com.stage.inex.domain.port.PasswordValidator;
import com.stage.inex.domain.repository.UserRepository;
import com.stage.inex.domain.service.UserRegistrationDomainService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRegistrationDomainServiceTests {

    @InjectMocks
    private UserRegistrationDomainService userRegistrationDomainService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordValidator passwordValidator;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @Tag(value = "failure")
    public void shouldThrowExceptionWhenEmailIsAlreadyTaken(){

        User user = new User("mali", "kenger", "test@gmail.com", new User.EncodedPassword("$2"));

        when(userRepository.findByEmail("existing@email.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () ->
                userRegistrationDomainService.registerUser(new UserRegistrationData("mali", "kenger", "existing@email.com", "123"))
    );
    }

    @Test
    @Tag(value = "success")
    public void shouldRegisterUserSuccessfully(){

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());

        when(passwordEncoder.encode("Zxcv123.4+_@*")).thenReturn("$2a$10$fakeHashedPassword");

        userRegistrationDomainService.registerUser(new UserRegistrationData("mali", "kenger", "test@email.com", "Zxcv123.4+_@*"));
    }
}

