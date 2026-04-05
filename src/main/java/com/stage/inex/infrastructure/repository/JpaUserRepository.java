package com.stage.inex.infrastructure.repository;

import com.stage.inex.domain.model.User;
import com.stage.inex.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
}
