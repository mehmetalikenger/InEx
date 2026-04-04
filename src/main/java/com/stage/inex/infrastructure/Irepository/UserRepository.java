package com.stage.inex.infrastructure.Irepository;

import com.stage.inex.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);
    Optional<User> findByEmail(String email);
}
