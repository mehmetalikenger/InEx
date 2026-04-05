package com.stage.inex.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name="income_categories")
public class IncomeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;
}
