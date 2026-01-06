package com.bankaccount.repository;

import com.bankaccount.Transaction;
import java.util.List;

public interface TransactionRepository {

    void save(Transaction transaction);

    List<Transaction> findAll();
}