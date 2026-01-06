package com.bankaccount;

import java.util.List;

public class StatementPrinter {

    private static final String HEADER = "Date       | Type       | Amount   | Balance";
    private static final String SEPARATOR = "-----------+------------+----------+---------";

    public void print(List<Transaction> transactions) {
        System.out.println(HEADER);
        System.out.println(SEPARATOR);

        for (Transaction tx : transactions) {
            String sign = tx.type() == TransactionType.DEPOSIT ? " " : "-";
            System.out.printf("%s | %-10s | %s%7d | %d%n",
                    tx.date(),
                    tx.type(),
                    sign,
                    Math.abs(tx.amount()),
                    tx.balanceAfter());
        }

        if (transactions.isEmpty()) {
            System.out.println("(no transactions)");
        }
    }
}