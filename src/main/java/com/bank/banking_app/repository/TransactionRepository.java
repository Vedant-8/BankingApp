package com.bank.banking_app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.banking_app.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderIdOrRecipientId(Long senderId, Long recipientId);
}
