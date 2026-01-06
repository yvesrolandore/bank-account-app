package com.bankaccount.command;

import com.bankaccount.Transaction;
import com.bankaccount.TransactionType;
import com.bankaccount.DateProvider;
import com.bankaccount.repository.TransactionRepository;
import com.bankaccount.exception.AmountExceedsLimitException;
import com.bankaccount.exception.InsufficientFundsException;
import com.bankaccount.exception.InvalidAmountException;

import java.time.LocalDate;

public class BankAccountCommandHandler {

    private static final int MAX_AMOUNT_PER_OPERATION = 1_000_000;

    private final DateProvider dateProvider;
    private final TransactionRepository repository;

    public BankAccountCommandHandler(DateProvider dateProvider, TransactionRepository repository) {
        this.dateProvider = dateProvider;
        this.repository = repository;
    }

    public void deposit(int amount) {
        validatePositiveAmount(amount);
        validateAmountLimit(amount);

        LocalDate today = dateProvider.currentDate();
        int newBalance = getBalance() + amount;

        repository.save(new Transaction(today, TransactionType.DEPOSIT, amount, newBalance));
    }

    public void withdraw(int amount) {
        validatePositiveAmount(amount);
        validateAmountLimit(amount);
        validateSufficientFunds(amount);

        LocalDate today = dateProvider.currentDate();
        int newBalance = getBalance() - amount;

        repository.save(new Transaction(today, TransactionType.WITHDRAWAL, -amount, newBalance));
    }

    private void validatePositiveAmount(int amount) {
        if (amount <= 0) throw new InvalidAmountException("Le montant doit être strictement positif. Montant fourni: " + amount);
    }

    private void validateAmountLimit(int amount) {
        if (amount > MAX_AMOUNT_PER_OPERATION) throw new AmountExceedsLimitException("Le montant dépasse la limite maximale de " + MAX_AMOUNT_PER_OPERATION + " par opération. Valeur fournie: " + amount);
    }

    private void validateSufficientFunds(int amount) {
        if (amount > getBalance()) throw new InsufficientFundsException("Fonds insuffisants. Solde actuel: " + getBalance() + ", retrait demandé: " + amount);
    }

    private int getBalance() {
        return repository.findAll().stream().mapToInt(Transaction::amount).sum();
    }
}