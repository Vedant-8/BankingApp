package com.bank.banking_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.banking_app.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
}
