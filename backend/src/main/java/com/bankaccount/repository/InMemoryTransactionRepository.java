package com.bankaccount.repository;

import com.bankaccount.Transaction;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTransactionRepository implements TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }
}
