package com.stage.inex.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private ExpenseCategory category;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private User user;

    @Column(precision = 19, scale = 4, nullable = false)
    @DecimalMin(value = "1", message = "Amount must be at least 1")
    @NotNull
    private BigDecimal amount;

    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.UNCONFIRMED;

    public void setAmount(BigDecimal amount){

        this.amount = amount;
    }

    public void setStartDate(LocalDate startDate){

        if(startDate == null){

            throw new IllegalArgumentException("Start date can't be null.");
        }

        if(startDate.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Start date can't be earlier then today.");
        }

        this.startDate = startDate;
    }

    public void updateStartDate(LocalDate newStartDate){

        if(this.startDate == null){

            throw new IllegalStateException("Start date is null. Use the setStartDate() method.");
        }

        if(newStartDate == null){

            throw new IllegalStateException("Start date can't be null.");
        }

        if(this.startDate.isBefore(LocalDate.now())){

            throw new IllegalStateException("Start date is passed. It cannot be changed.");
        }

        if(newStartDate.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Start date can't be earlier than today.");
        }

        this.startDate = newStartDate;
    }
}
