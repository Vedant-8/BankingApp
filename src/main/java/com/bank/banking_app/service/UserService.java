package com.bank.banking_app.service;

import com.bank.banking_app.dto.UserDto;
import com.bank.banking_app.entity.User;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto signin(String username, String password);
    UserDto getUserById(Long id);
    UserDto deposit(Long id, double amount);
    UserDto withdraw(Long id, double amount);
    UserDto addPoints(Long id, int points);
    UserDto addMoneyToWallet(Long userId, double amount);
    UserDto depositToBank(Long userId, double amount);
    void sendMoney(Long senderId, Long recipientId, double amount);
    void awardPoints(User user, double transactionAmount, int pointsPerTenDollars);
}
