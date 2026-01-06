package com.bankaccount.controller;

import com.bankaccount.Transaction;
import com.bankaccount.command.BankAccountCommandHandler;
import com.bankaccount.query.BankAccountQueryHandler;
import com.bankaccount.repository.InMemoryTransactionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(
    name = "Bank Account",
    description = "Endpoints for managing a simple bank account using CQRS and Event Sourcing"
)
public class BankAccountController {

    private final BankAccountCommandHandler commandHandler;
    private final BankAccountQueryHandler queryHandler;

    public BankAccountController() {
        InMemoryTransactionRepository repository = new InMemoryTransactionRepository();
        this.commandHandler = new BankAccountCommandHandler(LocalDate::now, repository);
        this.queryHandler = new BankAccountQueryHandler(repository);
    }

    @Operation(summary = "Get all account transactions")
    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return queryHandler.getTransactions();
    }

    @Operation(summary = "Get current account balance")
    @GetMapping("/balance")
    public int getBalance() {
        return queryHandler.getBalance();
    }

    @Operation(summary = "Deposit money into the account")
    @PostMapping("/deposit")
    public void deposit(@RequestParam int amount) {
        commandHandler.deposit(amount);
    }

    @Operation(summary = "Withdraw money from the account")
    @PostMapping("/withdraw")
    public void withdraw(@RequestParam int amount) {
        commandHandler.withdraw(amount);
    }
}