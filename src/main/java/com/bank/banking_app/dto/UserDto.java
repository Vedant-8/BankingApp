package com.bank.banking_app.dto;

import com.bank.banking_app.entity.User;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private double bankBalance;
    private double walletBalance;
    private int points;
    private String password; // Add this field if needed

    // No-argument constructor required for frameworks like Spring
    public UserDto() {
        // Initialize default values if necessary
    }

    // Constructor to map User entity to UserDto
    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.bankBalance = user.getBankBalance();
        this.walletBalance = user.getWalletBalance();
        this.points = user.getPoints();
    }

    // Remove the UnsupportedOperationException in the getPassword method
    public String getPassword() {
        return password; // Or implement a secure handling mechanism if needed
    }
}
