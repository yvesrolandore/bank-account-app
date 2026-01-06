package com.bankaccount;

public interface BankAccount {
    void deposit(int amount);
    void withdraw(int amount);
    void printStatement();
}