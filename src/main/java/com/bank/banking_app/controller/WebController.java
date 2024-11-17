package com.bank.banking_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.banking_app.dto.AccountDto;
import com.bank.banking_app.service.AccountService;

import java.util.List;

@Controller
public class WebController {

    private final AccountService accountService;

    public WebController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("title", "Banking Application");
        return "index";
    }

    @GetMapping("/accounts")
    public String viewAccounts(Model model) {
        List<AccountDto> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "accounts";
    }

    @GetMapping("/accounts/new")
    public String addAccountForm(Model model) {
        model.addAttribute("account", new AccountDto());
        return "add_account";
    }

    @PostMapping("/accounts")
    public String saveAccount(AccountDto accountDto) {
        accountService.createAccount(accountDto);
        return "redirect:/accounts";
    }

    @GetMapping("/accounts/{id}")
    public String viewAccountDetails(@PathVariable Long id, Model model) {
        AccountDto account = accountService.getAccountById(id);
        model.addAttribute("account", account);
        return "account_details";
    }

    @GetMapping("/accounts/{id}/deposit")
    public String depositForm(@PathVariable Long id, Model model) {
        model.addAttribute("accountId", id);
        return "deposit";
    }

    @PostMapping("/accounts/{id}/deposit")
    public String deposit(@PathVariable Long id, @RequestParam double amount) {
        accountService.deposit(id, amount);
        return "redirect:/accounts/" + id;
    }

    @GetMapping("/accounts/{id}/withdraw")
    public String withdrawForm(@PathVariable Long id, Model model) {
        model.addAttribute("accountId", id);
        return "withdraw";
    }

    @PostMapping("/accounts/{id}/withdraw")
    public String withdraw(@PathVariable Long id, @RequestParam double amount) {
        accountService.withdraw(id, amount);
        return "redirect:/accounts/" + id;
    }

    @GetMapping("/accounts/{id}/delete")
    public String deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return "redirect:/accounts";
    }
}
