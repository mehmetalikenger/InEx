package com.stage.inex.domain.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="income_categories")
public class IncomeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    protected IncomeCategory() {};

    public IncomeCategory(String name) {

        this.name = Objects.requireNonNull(name);
    };
}
