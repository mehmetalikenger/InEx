package com.stage.inex.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses", indexes = {
        @Index(name = "idx_expenses_user_id", columnList = "user")
        })
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

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Status status = Status.UNCONFIRMED;
}
