package com.stage.inex.domain.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "expense_categories")
public class ExpenseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    protected ExpenseCategory() {};

    public ExpenseCategory(String name) {

        this.name = Objects.requireNonNull(name);
    };
}
