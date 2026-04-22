package com.stage.inex.unitTest;

import com.stage.inex.domain.data.UserAuthData;
import com.stage.inex.domain.model.User;
import com.stage.inex.domain.port.PasswordEncoder;
import com.stage.inex.domain.port.TokenGenerator;
import com.stage.inex.domain.repository.UserRepository;
import com.stage.inex.domain.service.UserAuthDomainService;
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
public class UserAuthDomainServiceTests {

    @InjectMocks
    UserAuthDomainService userAuthDomainService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    TokenGenerator tokenGenerator;

    @Test
    @Tag(value="failure")
    public void shouldThrowWhenEmailDoesNotExistInUsersTable(){

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userAuthDomainService.authenticate(new UserAuthData("test@email.com", "123")));
    }

    @Test
    @Tag(value = "success")
    public void shouldAuthenticateUserSuccessful(){

        User user = new User("mali", "kenger", "testqgmail.com", new User.EncodedPassword("$2"));

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        userAuthDomainService.authenticate(new UserAuthData("test@email.com", "123"));
    }
}
