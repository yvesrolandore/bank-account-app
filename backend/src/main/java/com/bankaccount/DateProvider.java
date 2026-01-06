package com.bankaccount;

import java.time.LocalDate;

@FunctionalInterface
public interface DateProvider {
    LocalDate currentDate();
}