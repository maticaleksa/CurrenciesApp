package com.example.currenciesapp.domain;

import java.util.Currency;

/**
 * Domain class for exchange rates with their base in EUR.
 * TODO maybe add a field for base.
 */
public class ExchangeRate {

    public final String base;
    public final Currency currency;
    public final double rate;

    public ExchangeRate(String base, Currency currency, double rate) {
        this.base = base;
        this.currency = currency;
        this.rate = rate;
    }
}
