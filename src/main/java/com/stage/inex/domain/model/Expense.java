package com.stage.inex.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Expense {

    public enum Status{
        CONFIRMED,
        UNCONFIRMED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ExpenseCategory category;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)

    @CreationTimestamp
    private LocalDate created_at;

    private Status status = Status.UNCONFIRMED;

    private LocalDate deleted_at;
}
