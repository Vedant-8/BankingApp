package com.bank.banking_app.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.banking_app.dto.UserDto;
import com.bank.banking_app.entity.TransactionType;
import com.bank.banking_app.service.TransactionService;
import com.bank.banking_app.service.UserService;

@Controller
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class); // Logger instance

    private final UserService userService;
    private final TransactionService transactionService;

    public WebController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    // Signup Page
    @GetMapping("/signup")
    public String signupPage(Model model) {
        logger.info("Accessing /signup endpoint for GET request");
        model.addAttribute("user", new UserDto()); // Add UserDto object to model
        return "signup"; // Render signup.html
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute UserDto userDto, Model model) {
        logger.info("Received POST request to /signup with UserDto: {}", userDto);
        try {
            userService.createUser(userDto);
            logger.info("User created successfully: {}", userDto.getUsername());
            return "redirect:/signin";
        } catch (RuntimeException e) {
            logger.error("Error occurred during signup: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    // Signin Page
    @GetMapping("/signin")
    public String signinPage() {
        logger.info("Accessing /signin endpoint for GET request");
        return "signin";
    }

    @PostMapping("/signin")
    public String signin(@RequestParam String username, @RequestParam String password, Model model) {
        logger.info("Received POST request to /signin with username: {}", username);
        try {
            UserDto user = userService.signin(username, password);
            logger.info("User signed in successfully: {}", username);
            return "redirect:/profile/" + user.getId();
        } catch (RuntimeException e) {
            logger.error("Error occurred during signin for username {}: {}", username, e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "signin";
        }
    }

    // Profile Page
    @GetMapping("/profile/{id}")
    public String profilePage(@PathVariable Long id, Model model) {
        UserDto user = userService.getUserById(id);
        if (user == null) {
            model.addAttribute("error", "User not found");
            return "error";
        }
        model.addAttribute("user", user);
        model.addAttribute("transactions", transactionService.getTransactionsForUser(id));
        return "profile";
    }

    @PostMapping("/wallet/add")
    public String addMoneyToWallet(
            @RequestParam Long userId,
            @RequestParam double amount,
            Model model
    ) {
        try {
            userService.addMoneyToWallet(userId, amount);
            return "redirect:/profile/" + userId;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", userService.getUserById(userId)); // Ensure user details are passed back
            return "profile";
        }
    }

    @PostMapping("/bank/deposit")
    public String depositToBank(
            @RequestParam Long userId,
            @RequestParam double amount,
            Model model
    ) {
        try {
            userService.depositToBank(userId, amount);
            return "redirect:/profile/" + userId;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", userService.getUserById(userId)); // Ensure user details are passed back
            return "profile";
        }
    }

    // Send Money - GET request for the send money page
    @GetMapping("/wallet/send")
    public String sendMoneyPage(@RequestParam Long userId, Model model) {
        UserDto user = userService.getUserById(userId);
        if (user == null) {
            model.addAttribute("error", "User not found");
            return "error";
        }
        model.addAttribute("user", user); // Pass the user to the view
        return "send-money"; // Render send-money.html
    }

    // Transaction History - GET request for transaction history page
    @GetMapping("/transactions")
public String transactionHistoryPage(@RequestParam Long userId, Model model) {
    UserDto user = userService.getUserById(userId);
    if (user == null) {
        model.addAttribute("error", "User not found");
        return "error";
    }
    model.addAttribute("user", user); // Pass the user to the view
    model.addAttribute("transactions", transactionService.getTransactionsForUser(userId)); // Pass the transactions to the view
    return "transactions"; // Render transactions.html
}


@PostMapping("/wallet/send")
public String sendMoney(
        @RequestParam Long senderId,
        @RequestParam Long recipientId,
        @RequestParam double amount,
        Model model
) {
    try {
        userService.sendMoney(senderId, recipientId, amount);
        transactionService.createTransaction(senderId, recipientId, amount, TransactionType.TRANSFER);
        return "redirect:/profile/" + senderId;
    } catch (RuntimeException e) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("user", userService.getUserById(senderId));
        return "profile";
    }
}

}

