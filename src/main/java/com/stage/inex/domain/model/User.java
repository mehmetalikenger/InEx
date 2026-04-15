package com.stage.inex.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;
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
    private HashedPassword password;

    @CreationTimestamp
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    private Status status = Status.UNCONFIRMED;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Income> incomes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Expense> expenses;

    private LocalDate deleted_at;

    protected User(){};

    public User(String name, String surname, String email, HashedPassword password){

        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    };

    public record HashedPassword(String hashedPassword){

        public HashedPassword {

            if(!hashedPassword.startsWith("$2")){

                throw new IllegalArgumentException("Invalid hash!");
            }
        }
    }

    public void updatePassword(HashedPassword hashedPassword){

        password = hashedPassword;
    }

    public void updateName(String name){

        this.name = name;
    }

    public void updateSurname(String surname){

        this.surname = surname;
    }

    public void email(String email){

        this.email = email;
    }

    public void confirmUser(){

        status = Status.CONFIRMED;
    }

    //Move this to domain service
    public void validatePassword(String rawPassword){

        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*._+]).{8,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rawPassword);

        if(!matcher.matches()){
            throw new IllegalArgumentException("Password doesn't meet the minimum requirements");
        }
    }
}
