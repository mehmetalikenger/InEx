package com.stage.inex.domain.service;

import com.stage.inex.domain.data.AuthResponseData;
import com.stage.inex.domain.port.PasswordEncoder;
import com.stage.inex.domain.data.UserAuthData;
import com.stage.inex.domain.exception.PasswordsDoNotMatchException;
import com.stage.inex.domain.exception.UserNotFoundException;
import com.stage.inex.domain.model.User;
import com.stage.inex.domain.port.TokenGenerator;
import com.stage.inex.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthDomainService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    public UserAuthDomainService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenGenerator tokenGenerator){

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
    }

    public AuthResponseData authenticate(UserAuthData data){

        Optional<User> dbUser = userRepository.findByEmail(data.email());

        if(dbUser.isEmpty()){

            throw new UserNotFoundException("User not found.");
        }

        User user = dbUser.get();

        passwordEncoder.matches(data.password(), user.getPassword());

        return new AuthResponseData(tokenGenerator.generateAccessToken(user.getEmail()),
                tokenGenerator.generateRefreshToken(user.getEmail()));
    }
}
