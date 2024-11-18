package com.bank.banking_app.service.impl;

import com.bank.banking_app.entity.Transaction;
import com.bank.banking_app.entity.TransactionType;
import com.bank.banking_app.repository.TransactionRepository;
import com.bank.banking_app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getTransactionsForUser(Long userId) {
        return transactionRepository.findBySenderIdOrRecipientId(userId, userId);
    }

    @Override
    public void createTransaction(Long senderId, Long recipientId, Double amount, TransactionType transactionType) {
        Transaction transaction = Transaction.builder()
                .senderId(senderId)
                .recipientId(recipientId)
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .transactionType(transactionType)
                .build();
        transactionRepository.save(transaction);
    }
}
