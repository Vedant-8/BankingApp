package com.bank.banking_app.service.impl;

import org.springframework.stereotype.Service;
import com.bank.banking_app.dto.UserDto;
import com.bank.banking_app.entity.TransactionType;
import com.bank.banking_app.entity.User;
import com.bank.banking_app.repository.UserRepository;
import com.bank.banking_app.service.UserService;
import com.bank.banking_app.service.TransactionService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TransactionService transactionService;

    public UserServiceImpl(UserRepository userRepository, TransactionService transactionService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setBankBalance(userDto.getBankBalance());
        user.setWalletBalance(0.00);
        user.setPoints(0);
        userRepository.save(user);
        return new UserDto(user);
    }

    @Override
    public UserDto signin(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user != null) {
            return new UserDto(user);
        } else {
            return null;
        }
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDto(user);
    }

    @Override
    public UserDto deposit(Long id, double amount) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setWalletBalance(user.getWalletBalance() + amount);
        userRepository.save(user);
        return new UserDto(user);
    }

    @Override
    public UserDto withdraw(Long id, double amount) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getWalletBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }
        user.setWalletBalance(user.getWalletBalance() - amount);
        userRepository.save(user);
        return new UserDto(user);
    }

    @Override
    public UserDto addPoints(Long id, int points) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPoints(user.getPoints() + points);
        userRepository.save(user);
        return new UserDto(user);
    }

    @Override
    public UserDto addMoneyToWallet(Long userId, double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getBankBalance() < amount) {
            throw new RuntimeException("Insufficient bank balance.");
        }

        user.setWalletBalance(user.getWalletBalance() + amount);
        user.setBankBalance(user.getBankBalance() - amount);
        awardPoints(user, amount, 1);
        userRepository.save(user);

        return new UserDto(user);
    }

    @Override
    public UserDto depositToBank(Long userId, double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getWalletBalance() < amount) {
            throw new RuntimeException("Insufficient wallet balance.");
        }

        user.setWalletBalance(user.getWalletBalance() - amount);
        user.setBankBalance(user.getBankBalance() + amount);
        awardPoints(user, amount, 1);
        userRepository.save(user);

        return new UserDto(user);
    }

    @Override
    public void sendMoney(Long senderId, Long recipientId, double amount) {
        if (senderId.equals(recipientId)) {
            throw new RuntimeException("Sender and recipient cannot be the same.");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        if (sender.getWalletBalance() < amount) {
            throw new RuntimeException("Insufficient wallet balance.");
        }

        // Deduct from sender's wallet
        sender.setWalletBalance(sender.getWalletBalance() - amount);
        // Add to recipient's wallet
        recipient.setWalletBalance(recipient.getWalletBalance() + amount);
        awardPoints(sender, amount, 2); // 2 points per $10 for sending money
        // Save both users
        userRepository.save(sender);
        userRepository.save(recipient);

        // Log the transaction
        transactionService.createTransaction(senderId, recipientId, amount, TransactionType.TRANSFER);
    }

    @Override
    public void awardPoints(User user, double transactionAmount, int pointsPerTenDollars) {
        int pointsToAward = (int) (transactionAmount / 10) * pointsPerTenDollars;
        user.setPoints(user.getPoints() + pointsToAward);
        userRepository.save(user);
    }
}
