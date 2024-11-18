package com.bank.banking_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "bank_balance", nullable = false, columnDefinition = "DECIMAL(15,2) DEFAULT 0.00")
    private double bankBalance;

    @Column(name = "wallet_balance", nullable = false, columnDefinition = "DECIMAL(15,2) DEFAULT 0.00")
    private double walletBalance;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int points;
}
