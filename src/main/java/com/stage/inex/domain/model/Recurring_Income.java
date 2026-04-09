package com.stage.inex.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="recurring_incomes", indexes = {
        @Index(name="idx_recurring_incomes_next_generation_date", columnList = "next_generation_date")
})
public class Recurring_Income {

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
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ExpenseCategory category;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private int frequency;

    @Column(nullable = false)
    private LocalDate start_date;

    @Column(nullable = false)
    private LocalDate end_date;

    @Column(nullable = false)
    private LocalDate next_generation_date;
}
