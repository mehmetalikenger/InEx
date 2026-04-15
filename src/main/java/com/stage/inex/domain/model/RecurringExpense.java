package com.stage.inex.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="recurring_expenses", indexes = {
        @Index(name = "idx_recurring_expenses_next_generation_date", columnList = "next_generation_date")
})
public class RecurringExpense {

    private enum Frequency{
        YEARLY,
        MONTHLY,
        WEEKLY,
        DAILY
    }

    public enum Status{
        ACTIVE,
        ENDED,
        CANCELED
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

    @Column(nullable = false)
    private LocalDate startingDate;

    private LocalDate endingDate;

    private int repeatCount = -1;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @Column(nullable = false)
    private LocalDate nextGenerationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDate statusUpdateDate;

    protected RecurringExpense(){};

    public RecurringExpense(String name, ExpenseCategory category, User user, BigDecimal amount, LocalDate startingDate, String frequency){
        this.name = Objects.requireNonNull(name);
        this.category = Objects.requireNonNull(category);
        this.amount = Objects.requireNonNull(amount);
        this.user = Objects.requireNonNull(user);

        if(startingDate.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Starting date can't be earlier than today.");
        }

        this.startingDate = Objects.requireNonNull(startingDate);
        nextGenerationDate = startingDate;

        try{

            this.frequency = Frequency.valueOf(Objects.requireNonNull(frequency).toUpperCase());

        } catch (IllegalArgumentException e){

            throw new IllegalArgumentException("Invalid frequency: " + frequency);
        }
    }

    public RecurringExpense(String name, ExpenseCategory category, User user, BigDecimal amount, LocalDate startingDate, String frequency, LocalDate endingDate){

        this(name, category, user, amount, startingDate, frequency);

        if(endingDate.isBefore(startingDate) || endingDate.isEqual(startingDate)){

            throw new IllegalStateException("Ending date must be after the start date.");
        }

        this.endingDate = endingDate;
    }

    public RecurringExpense(String name, ExpenseCategory category, User user, BigDecimal amount, LocalDate startingDate, String frequency, int repeatCount){

        this(name, category, user, amount, startingDate, frequency);

        if(repeatCount <= 1){

            throw new IllegalArgumentException("Repeat count must be at least 2.");
        }

        this.repeatCount = repeatCount;
    }

    public void updateDates(LocalDate newStartingDate, LocalDate newEndingDate){    // To update the startDate. Updating the startDate requires updating the endDate.

        if(newStartingDate == null){

            throw new IllegalStateException("Starting date can't be null.");
        }

        if(newStartingDate.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Starting date can't be earlier than today.");
        }

        if(newEndingDate.isBefore(newStartingDate)){

            throw new IllegalStateException("Ending date must be after the starting date.");
        }

        this.startingDate = newStartingDate;
        nextGenerationDate = newStartingDate;

        this.endingDate = newEndingDate;
    }

    public void updateFrequency(String newFrequency){

        if(newFrequency == null){

            throw new IllegalArgumentException("Frequency cannot be null.");
        }

        if(startingDate.isAfter(LocalDate.now())){

            try{

                frequency = Frequency.valueOf(newFrequency.toUpperCase());

            } catch (IllegalArgumentException e){

                throw new IllegalArgumentException("Invalid frequency: " + newFrequency);
            }

        } else {

            throw new IllegalStateException("The frequency cannot be updated because the starting date has passed.");
        }
    }

    private void setNextGenerationDate(){

        if(frequency == null){

            throw new IllegalStateException("Next generation date cannot be set when frequency is null.");
        }

        switch (frequency){

            case DAILY -> nextGenerationDate = nextGenerationDate.plusDays(1);
            case WEEKLY -> nextGenerationDate = nextGenerationDate.plusWeeks(1);
            case MONTHLY -> nextGenerationDate = nextGenerationDate.plusMonths(1);
            case YEARLY -> nextGenerationDate = nextGenerationDate.plusYears(1);
        }

       if(endingDate != null){

           if(nextGenerationDate.isAfter(endingDate)){

               status = Status.ENDED;
           }
       }
    }

    public void activate(){

        status = Status.ACTIVE;
    }

    public void update(){        // After the expense is added to the Expenses table.

        if(repeatCount > 0){
            repeatCount--;
        }

        if(repeatCount == 0){

            status = Status.ENDED;
            return;
        }

        setNextGenerationDate();

        if(endingDate != null){

            if(nextGenerationDate.isAfter(endingDate)){

                status = Status.ENDED;
            }
        }
    }
}
