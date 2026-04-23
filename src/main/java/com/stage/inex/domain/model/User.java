package com.stage.inex.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="users")
public class User implements UserDetails {

    public enum Status {
        CONFIRMED,
        UNCONFIRMED,
        DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String surname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    private Status status = Status.UNCONFIRMED;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Income> incomes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Expense> expenses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RecurringIncome> recurringIncomes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RecurringExpense> recurringExpenses;

    private LocalDate deletedAt;

    protected User(){};

    public User(String name, String surname, String email, EncodedPassword password){

        this.name = Objects.requireNonNull(name);
        this.surname = surname;
        this.email = Objects.requireNonNull(email);
        this.password = password.encodedPassword;
    };

    public record EncodedPassword(String encodedPassword){

        public EncodedPassword {

            if(!encodedPassword.startsWith("$2")){

                throw new IllegalArgumentException("Invalid hash!");
            }
        }
    }

    public void updatePassword(EncodedPassword hashedPassword){

        password = hashedPassword.encodedPassword;
    }

    public void updateName(String name){

        this.name = name;
    }

    public void updateSurname(String surname){

        this.surname = surname;
    }

    public void updateEmail(String email){

        this.email = email;
    }

    public void confirmUser(){

        status = Status.CONFIRMED;
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    @Override
    @NonNull
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {

        return status != Status.DELETED;
    }

    public String getEmail() {
        return email;
    }
}
