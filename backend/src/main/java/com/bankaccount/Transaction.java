package com.bankaccount;
import java.time.LocalDate;

public record Transaction(
        LocalDate date,
        TransactionType type,
        int amount,
        int balanceAfter) {
}