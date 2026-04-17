package com.stage.inex.domain.service;

import com.stage.inex.domain.PasswordEncoder;
import com.stage.inex.domain.data.UserRegistrationData;
import com.stage.inex.domain.exception.EmailAlreadyTakenException;
import com.stage.inex.domain.model.User;
import com.stage.inex.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserRegistrationDomainService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserRegistrationDomainService(PasswordEncoder passwordEncoder, UserRepository userRepository){

        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void registerUser(UserRegistrationData rd){

        Optional<User> existingUser = userRepository.findByEmail(rd.email());

        if(existingUser.isPresent()){

            throw new EmailAlreadyTakenException("Email has already been taken");
        }

        validatePassword(rd.password());

        String hashedPassword = passwordEncoder.encode(rd.password());

        User.HashedPassword validatedHashedPassword = new User.HashedPassword(hashedPassword);

        User user = new User(rd.name(), rd.surname(), rd.email(), validatedHashedPassword);

        userRepository.save(user);
    }

    private void validatePassword(String rawPassword){

        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*._+]).{8,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rawPassword);

        if(!matcher.matches()){
            throw new IllegalArgumentException("Password doesn't meet the minimum requirements");
        }
    }
}
