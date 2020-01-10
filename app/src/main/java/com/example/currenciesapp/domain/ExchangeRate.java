package com.example.currenciesapp.domain;

/**
 * Domain class for exchange rates with their base in EUR.
 * TODO maybe add a field for base.
 */
public class ExchangeRate {

    public final String currencyCode;
    public final double rate;

    public ExchangeRate(String currencyCode, double rate) {
        this.currencyCode = currencyCode;
        this.rate = rate;
    }
}
