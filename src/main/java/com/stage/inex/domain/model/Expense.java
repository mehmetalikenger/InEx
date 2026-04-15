package com.stage.inex.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "expenses", indexes = {
        @Index(name = "idx_expenses_user_id", columnList = "user_id"),
        @Index(name="idx_expenses_initial_date", columnList = "initial_date")
        })
public class Expense {

    public enum Status{
        CONFIRMED,
        UNCONFIRMED,
        PENDING,
        CANCELED,
        REVERTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ExpenseCategory category;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(precision = 19, scale = 4, nullable = false)
    @DecimalMin(value = "1", message = "Amount must be at least 1")
    private BigDecimal amount;

    @CreationTimestamp
    private LocalDate createdAt;

    private LocalDate initialDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDate statusUpdateDate;

    protected Expense(){};

    public Expense(String name, User user, BigDecimal amount, ExpenseCategory category) {
        this.name = Objects.requireNonNull(name);
        this.category = Objects.requireNonNull(category);
        this.amount = Objects.requireNonNull(amount);
        this.user = Objects.requireNonNull(user);
    }

    public Expense(String name, User user, BigDecimal amount, ExpenseCategory category, LocalDate initialDate) {
        this(name, user, amount, category);

        if(initialDate.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Initial date can't be earlier than today.");
        }

        this.initialDate = Objects.requireNonNull(initialDate);
    }

    public void setInitialDate(LocalDate initialDate){

        if(initialDate == null){

            throw new IllegalArgumentException("Initial date can't be null.");
        }

        if(initialDate.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Initial date can't be earlier then today.");
        }

        this.initialDate = initialDate;
    }

    public void updateInitialDate(LocalDate newInitialDate){

        if(initialDate == null){

            throw new IllegalStateException("Initial date is null. Use the setInitialDate method.");
        }

        if(newInitialDate == null){

            throw new IllegalArgumentException("Initial date can't be null.");
        }

        if(this.initialDate.isBefore(LocalDate.now())){

            throw new IllegalStateException("Initial date is passed. It cannot be changed.");
        }

        if(newInitialDate.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Initial date can't be earlier than today.");
        }

        this.initialDate = newInitialDate;
    }

    public void activate(){   // To finalize a record without an initial date.

        status = Status.CONFIRMED;

        statusUpdateDate = LocalDate.now();
    }

    public void start(){   // To finalize a record with an initial date.

        if(initialDate == null){

            throw new IllegalStateException("Use the activate method() for the records without an initial date.");

        } else {

            if(initialDate.isEqual(LocalDate.now())){

                status = Status.UNCONFIRMED;

            } else if(initialDate.isAfter(LocalDate.now())){

                status = Status.PENDING;
            }

            statusUpdateDate = LocalDate.now();
        }
    }

    public void updateScheduledExpenseStatus(){

        if(status != Status.PENDING){

            throw new IllegalStateException("Only PENDING expenses can be updated to active.");
        }

        if(initialDate.isAfter(LocalDate.now())){

            throw new IllegalStateException("The initial date hasn't came yet.");
        }

        if(initialDate.isBefore(LocalDate.now())){

            throw new IllegalStateException("The initial date has passed.");
        }

        status = Status.UNCONFIRMED;
        this.statusUpdateDate = LocalDate.now();
    }

    public void cancel(){

        if(status == Status.CONFIRMED){

            throw new IllegalStateException("Confirmed expense record can't be canceled.");
        }

        status = Status.CANCELED;

        statusUpdateDate = LocalDate.now();
    }


    public void revert(){

        if(status == Status.PENDING || status == Status.UNCONFIRMED){

            throw new IllegalStateException("Only confirmed expense records can be reverted.");
        }

        status = Status.REVERTED;

        statusUpdateDate = LocalDate.now();
    }
}
