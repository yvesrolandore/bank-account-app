package com.bankaccount;

import com.bankaccount.exception.AmountExceedsLimitException;
import com.bankaccount.exception.InsufficientFundsException;
import com.bankaccount.exception.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.bankaccount.repository.InMemoryTransactionRepository;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    private DateProvider fixedDateProvider;
    private StatementPrinter printer;
    private BankAccount account;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        fixedDateProvider = () -> LocalDate.of(2024, 1, 15);
        printer = new StatementPrinter();
        
        account = new BankAccountImpl(
            fixedDateProvider,
            printer,
            new InMemoryTransactionRepository()
        );

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void deposit_valid_amount_updates_balance_and_records_transaction() {
        account.deposit(1000);
        account.deposit(500);
        account.printStatement();
        String output = outputStream.toString();

        assertTrue(output.contains("1000"));
        assertTrue(output.contains("1500"));
    }

    @Test
    void deposit_zero_amount_throws_exception() {
        InvalidAmountException ex = assertThrows(InvalidAmountException.class, () -> account.deposit(0));
        assertEquals("Amount must be strictly positive. Provided: 0", ex.getMessage());
    }

    @Test
    void deposit_negative_amount_throws_exception() {
        InvalidAmountException ex = assertThrows(InvalidAmountException.class, () -> account.deposit(-100));
        assertEquals("Amount must be strictly positive. Provided: -100", ex.getMessage());
    }

    @Test
    void deposit_exceeding_limit_throws_exception() {
        AmountExceedsLimitException ex = assertThrows(AmountExceedsLimitException.class,
                () -> account.deposit(1_000_001));
        assertTrue(ex.getMessage().contains("exceeds the maximum limit of 1000000"));
    }

    @Test
    void withdraw_valid_amount_with_sufficient_funds_updates_balance() {
        account.deposit(2000);
        account.withdraw(500);

        account.printStatement();
        String output = outputStream.toString();
        assertTrue(output.contains("Balance"));
        assertTrue(output.contains("1500"));
    }

    @Test
    void withdraw_zero_amount_throws_exception() {
        account.deposit(1000);
        assertThrows(InvalidAmountException.class, () -> account.withdraw(0));
    }

    @Test
    void withdraw_negative_amount_throws_exception() {
        account.deposit(1000);
        assertThrows(InvalidAmountException.class, () -> account.withdraw(-50));
    }

    @Test
    void withdraw_more_than_balance_throws_insufficient_funds() {
        account.deposit(500);
        InsufficientFundsException ex = assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(600));
        assertTrue(ex.getMessage().contains("Insufficient funds"));
    }

    @Test
    void withdraw_exact_balance_leaves_zero() {
        account.deposit(1000);
        account.withdraw(1000);

        account.printStatement();
        String output = outputStream.toString();
        assertTrue(output.contains("0"));
    }

    @Test
    void withdraw_exceeding_limit_throws_exception() {
        account.deposit(900_000);
        assertThrows(AmountExceedsLimitException.class, () -> account.withdraw(1_000_001));
    }

    @Test
    void print_statement_on_empty_account_shows_no_transactions() {
        account.printStatement();
        String output = outputStream.toString();
        assertTrue(output.contains("(no transactions)") || output.contains("Date       | Type"));
    }

   @Test
    void print_statement_with_multiple_transactions_in_reverse_chronological_order() {
        LocalDate[] dates = {
                LocalDate.of(2024, 1, 10),
                LocalDate.of(2024, 1, 12),
                LocalDate.of(2024, 1, 15)
        };
        int[] index = {0};
        DateProvider sequentialProvider = () -> dates[index[0]++];

        BankAccount seqAccount = new BankAccountImpl(
            sequentialProvider,
            printer,
            new InMemoryTransactionRepository()
        );

        seqAccount.deposit(1000);
        seqAccount.deposit(2000);
        seqAccount.withdraw(500);

        seqAccount.printStatement();
        String output = outputStream.toString();
        assertTrue(output.contains("2024-01-15"), "La date la plus récente doit apparaître");
        assertTrue(output.contains("2024-01-12"), "Date intermédiaire doit apparaître");
        assertTrue(output.contains("2024-01-10"), "Date la plus ancienne doit apparaître");

        assertTrue(output.contains("WITHDRAWAL"), "Doit contenir un retrait");
        assertTrue(output.indexOf("2024-01-15") < output.indexOf("2024-01-12"), "Le retrait (15 janv) doit apparaître avant le dépôt du 12");
        assertTrue(output.indexOf("2024-01-12") < output.indexOf("2024-01-10"), "Le dépôt du 12 doit apparaître avant celui du 10");
        assertTrue(output.contains("2500"), "Solde final après retrait doit être 2500");
        assertTrue(output.contains("3000"), "Solde intermédiaire doit être 3000");
        assertTrue(output.contains("1000"), "Solde initial doit être 1000");
    }

    @Test
    void full_integration_scenario_deposit_withdraw_deposit_then_statement() {
        int deposit1 = 3000;
        int withdraw = 500;
        int deposit2 = 1000;

        account.deposit(deposit1);
        account.withdraw(withdraw);
        account.deposit(deposit2);

        account.printStatement();
        String output = outputStream.toString();

        int finalBalance = deposit1 - withdraw + deposit2;

        assertTrue(output.contains(String.valueOf(finalBalance)),
            "Le relevé doit contenir le solde final après les opérations");

        assertTrue(output.contains("DEPOSIT"), "Doit contenir au moins un dépôt");
        assertTrue(output.contains("WITHDRAWAL"), "Doit contenir un retrait");
    }
}