package com.bankaccount.exception;

public class AmountExceedsLimitException extends RuntimeException {
    public AmountExceedsLimitException(String message) {
        super(message);
    }
}