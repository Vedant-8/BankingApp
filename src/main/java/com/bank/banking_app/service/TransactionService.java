package com.bank.banking_app.service;

import java.util.List;
import com.bank.banking_app.entity.Transaction;
import com.bank.banking_app.entity.TransactionType;

public interface TransactionService {
    List<Transaction> getTransactionsForUser(Long userId);

    void createTransaction(Long senderId, Long recipientId, Double amount, TransactionType transactionType);
}
