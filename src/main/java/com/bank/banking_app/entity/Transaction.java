package com.bank.banking_app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId;  // User who is sending money

    @Column(nullable = false)
    private Long recipientId;  // User who is receiving money

    @Column(nullable = false)
    private Double amount;  // The amount of money in the transaction

    @Column(nullable = false)
    private LocalDateTime transactionDate;  // Date and time when the transaction occurred

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;  // Type of transaction (Deposit, Withdrawal, Transfer)

}
