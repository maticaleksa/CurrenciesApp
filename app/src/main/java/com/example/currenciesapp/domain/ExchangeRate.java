package com.example.currenciesapp.domain;

import java.util.Currency;

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
