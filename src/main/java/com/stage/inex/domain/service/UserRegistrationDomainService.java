package com.stage.inex.domain.service;

import com.stage.inex.domain.port.PasswordEncoder;
import com.stage.inex.domain.data.UserRegistrationData;
import com.stage.inex.domain.exception.EmailAlreadyTakenException;
import com.stage.inex.domain.model.User;
import com.stage.inex.domain.port.PasswordValidator;
import com.stage.inex.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRegistrationDomainService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PasswordValidator passwordValidator;

    public UserRegistrationDomainService(PasswordEncoder passwordEncoder, UserRepository userRepository, PasswordValidator passwordValidator){

        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.passwordValidator = passwordValidator;
    }

    public void registerUser(UserRegistrationData rd){

        Optional<User> existingUser = userRepository.findByEmail(rd.email());

        if(existingUser.isPresent()){

            throw new EmailAlreadyTakenException("Email is already taken.");
        }

        passwordValidator.validate(rd.password());

        String encodedPassword = passwordEncoder.encode(rd.password());

        User.EncodedPassword validatedEncodedPassword = new User.EncodedPassword(encodedPassword);

        User user = new User(rd.name(), rd.surname(), rd.email(), validatedEncodedPassword);

        userRepository.save(user);
    }
}
