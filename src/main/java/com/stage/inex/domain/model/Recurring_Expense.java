package com.stage.inex.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="recurring_expenses", indexes = {
        @Index(name = "idx_recurring_expenses_next_generation_date", columnList = "nextGenerationDate")
})
public class Recurring_Expense {

    private enum Frequency{
        YEARLY,
        MONTHLY,
        WEEKLY,
        DAILY
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

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @Column(nullable = false)
    @NotNull
    private LocalDate startDate;

    @Column(nullable = false)
    @NotNull
    private LocalDate endDate;

    @Column(nullable = false)
    @NotNull
    private LocalDate nextGenerationDate;

    public void setAmount(BigDecimal amount){

        this.amount = amount;
    }

    public void setStartDate(LocalDate startDate){

        if(startDate.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Start date can't be earlier than today.");
        }

        this.startDate = startDate;
        nextGenerationDate = startDate;
    }

    public void setEndDate(LocalDate endDate){

        if(endDate.isBefore(this.startDate) || endDate.isEqual(this.startDate)){

            throw new IllegalStateException("Start date must be set before setting end date.");
        }

        this.endDate = endDate;
    }

    public void setFrequency(String frequency){

        if(frequency == null){

            throw new IllegalArgumentException("Frequency cannot be null.");
        }

        try{

            this.frequency = Frequency.valueOf(frequency.toUpperCase());

        } catch (IllegalArgumentException e){

            throw  new IllegalArgumentException("Invalid frequency: " + frequency);
        }
    }

    public void setNextGenerationDate(){

        if(frequency == null){

            throw new IllegalStateException("Next generation date cannot be set when frequency is null.");
        }

        switch (frequency){

            case DAILY -> nextGenerationDate = nextGenerationDate.plusDays(1);
            case WEEKLY -> nextGenerationDate = nextGenerationDate.plusWeeks(1);
            case MONTHLY -> nextGenerationDate = nextGenerationDate.plusMonths(1);
            case YEARLY -> nextGenerationDate = nextGenerationDate.plusYears(1);
        }
    }
}
