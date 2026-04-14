package com.stage.inex.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Frequency;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="recurring_expenses", indexes = {
        @Index(name = "idx_recurring_expenses_next_generation_date", columnList = "nextGenerationDate")
})
public class RecurringExpense {

    private enum Frequency{
        YEARLY,
        MONTHLY,
        WEEKLY,
        DAILY
    }

    private enum Status{
        ACTIVE,
        ENDED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private LocalDate startingDate = null;

    private LocalDate endingDate = null;

    private int repeatCount = -1;

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @Column(nullable = false)
    @NotNull
    private LocalDate nextGenerationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Status status;

    public void setAmount(BigDecimal amount){

        if(amount == null){

            throw  new IllegalArgumentException("Amount can't be null.");
        }

        this.amount = amount;
    }

    public void setStartingDate(LocalDate startingDate){

        if(this.startingDate != null){

            throw new IllegalStateException("To update the starting date, use the updateDates() method.");
        }

        if(startingDate == null){

            throw  new IllegalArgumentException("Starting date can't be null.");
        }

        if(startingDate.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Starting date can't be earlier than today.");
        }

        this.startingDate = startingDate;
        nextGenerationDate = startingDate;
    }

    public void updateDates(LocalDate newStartingDate, LocalDate newEndingDate){    // To update the startDate. Updating the startDate requires updating the endDate.

        if(this.startingDate == null){

            throw new IllegalStateException("Start date is null. Use the setStartDate() method.");
        }

        if(newStartingDate == null){

            throw new IllegalStateException("Start date can't be null.");
        }

        if(newStartingDate.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Start date can't be earlier than today.");
        }

        if(newEndingDate.isBefore(newStartingDate)){

            throw new IllegalStateException("End date must be after the start date.");
        }

        this.startingDate = newStartingDate;
        nextGenerationDate = newStartingDate;

        this.endingDate = newEndingDate;
    }

    public void setEndingDate(LocalDate endingDate){  // For both set and update

        if(startingDate== null){
            throw new IllegalStateException("Starting date must be set before setting the ending date.");
        }

        if(endingDate == null){
            throw new IllegalArgumentException("Ending date must be provided when updating starting date.");
        }

        if(endingDate.isBefore(startingDate) || endingDate.isEqual(startingDate)){

            throw new IllegalStateException("Ending date must be after the start date.");
        }

        if(repeatCount != -1){

            throw new IllegalStateException("Ending date cannot be set if the repeat count is set.");
        }

        this.endingDate = endingDate;
    }

    public void setRepeatCount(int rc){

        if(endingDate != null){

            throw new IllegalStateException("Repeat count cannot be set if the ending date is set.");
        }

        if(rc <= 1){

            throw new IllegalArgumentException("Repeat count must be at least 2.");
        }

        repeatCount = rc;
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

    public void updateFrequency(String newFrequency){

        if(startingDate.isAfter(LocalDate.now())){

            try{

                frequency = Frequency.valueOf(newFrequency);

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

    public void active(){

        status = Status.ACTIVE;
    }

    public void update(){

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
