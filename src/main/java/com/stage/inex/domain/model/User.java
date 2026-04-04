package com.stage.inex.domain.model;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name="users")
public class User {

    public enum Status {
        CONFIRMED,
        UNCONFIRMED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    private Status status = Status.UNCONFIRMED;


    void validatePassword(String rawPassword){

        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*._+]).{8,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rawPassword);

        if(!matcher.matches()){
            throw new IllegalArgumentException("Password doesn't meet the minimum requirements");
        }
    }

    void setHashedPassword(String hashedPassword){
        password = hashedPassword;
    }
}
