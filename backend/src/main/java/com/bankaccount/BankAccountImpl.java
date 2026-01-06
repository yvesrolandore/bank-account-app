package com.bankaccount;

import com.bankaccount.exception.AmountExceedsLimitException;
import com.bankaccount.exception.InsufficientFundsException;
import com.bankaccount.exception.InvalidAmountException;
import com.bankaccount.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankAccountImpl implements BankAccount {

    private static final int MAX_AMOUNT_PER_OPERATION = 1_000_000;

    private final TransactionRepository repository;
    private final DateProvider dateProvider;
    private final StatementPrinter printer;

    public BankAccountImpl(DateProvider dateProvider,
                           StatementPrinter printer,
                           TransactionRepository repository) {
        this.dateProvider = dateProvider;
        this.printer = printer;
        this.repository = repository;
    }

    @Override
    public void deposit(int amount) {
        validatePositiveAmount(amount);
        validateAmountLimit(amount);

        LocalDate today = dateProvider.currentDate();
        int newBalance = getBalance() + amount;

        repository.save(
            new Transaction(today, TransactionType.DEPOSIT, amount, newBalance)
        );
    }

    @Override
    public void withdraw(int amount) {
        validatePositiveAmount(amount);
        validateAmountLimit(amount);
        validateSufficientFunds(amount);

        LocalDate today = dateProvider.currentDate();
        int newBalance = getBalance() - amount;

        repository.save(
            new Transaction(today, TransactionType.WITHDRAWAL, -amount, newBalance)
        );
    }

    @Override
    public void printStatement() {
        List<Transaction> reversed = new ArrayList<>(repository.findAll());
        Collections.reverse(reversed);
        printer.print(reversed);
    }

    public int getBalance() {
        return repository.findAll()
                .stream()
                .mapToInt(Transaction::amount)
                .sum();
    }

    private void validatePositiveAmount(int amount) {
        if (amount <= 0) {
            throw new InvalidAmountException(
                String.format("Amount must be strictly positive. Provided: %d", amount)
            );
        }
    }

    private void validateAmountLimit(int amount) {
        if (amount > MAX_AMOUNT_PER_OPERATION) {
            throw new AmountExceedsLimitException(
                String.format(
                    "Amount exceeds the maximum limit of %d per operation. Provided: %d",
                    MAX_AMOUNT_PER_OPERATION,
                    amount
                )
            );
        }
    }

    private void validateSufficientFunds(int amount) {
        if (amount > getBalance()) {
            throw new InsufficientFundsException(
                String.format(
                    "Insufficient funds. Balance: %d, Requested: %d",
                    getBalance(),
                    amount
                )
            );
        }
    }

    public List<Transaction> getTransactions() {
        return repository.findAll();
    }
}
