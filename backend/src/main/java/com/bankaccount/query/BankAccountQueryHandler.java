package com.bankaccount.query;

import com.bankaccount.Transaction;
import com.bankaccount.repository.TransactionRepository;

import java.util.Collections;
import java.util.List;

public class BankAccountQueryHandler {

    private final TransactionRepository repository;

    public BankAccountQueryHandler(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> getTransactions() {
        List<Transaction> all = repository.findAll();
        Collections.reverse(all);
        return all;
    }

    public int getBalance() {
        return repository.findAll().stream().mapToInt(Transaction::amount).sum();
    }
}
