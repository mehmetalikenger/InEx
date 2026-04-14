package com.stage.inex.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "incomes", indexes = {
        @Index(name = "idx_incomes_user_id", columnList = "user"),
        @Index(name="idx_incomes_starting_date", columnList = "date")
        })
public class Income {

    public enum Status{
        CONFIRMED,
        UNCONFIRMED,
        PENDING
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
    private IncomeCategory category;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private User user;

    @Column(precision = 19, scale = 4, nullable = false)
    @DecimalMin(value = "1", message = "Amount must be at least 1")
    @NotNull
    private BigDecimal amount;

    private LocalDate date = null;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void setAmount(BigDecimal amount){

        this.amount = amount;
    }

    public void setDate(LocalDate date){

        if(date == null){

            throw new IllegalArgumentException("Start date can't be null.");
        }

        if(date.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Start date can't be earlier then today.");
        }

        if(date.isEqual(LocalDate.now())){

            status = Status.UNCONFIRMED;

        } else {

            status = Status.PENDING;
        }

        this.date = date;
    }

    public void updateStartDate(LocalDate newDate){

        if(this.date == null){

            throw new IllegalStateException("Date is null. Use the setDate() method.");
        }

        if(newDate == null){

            throw new IllegalStateException("Date can't be null.");
        }

        if(this.date.isBefore(LocalDate.now())){

            throw new IllegalStateException("Date is passed. It cannot be changed.");
        }

        if(newDate.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Date can't be earlier than today.");
        }

        this.date = newDate;
    }
}
