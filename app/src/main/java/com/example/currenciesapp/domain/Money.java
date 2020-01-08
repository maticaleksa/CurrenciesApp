package com.example.currenciesapp.domain;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Domain class that represents Money.
 */
public class Money {

    public final BigDecimal amount;
    public final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
}
