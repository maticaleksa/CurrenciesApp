package com.example.currenciesapp.currencies_screen;

/**
 * Data class that is used to display currencies.
 */
public class ExchangeRatesViewModel {
    public final String currencyCode;
    public final String currency;
    public final double rate;

    public ExchangeRatesViewModel(String currencyCode, String currency, double rate) {
        this.currencyCode = currencyCode;
        this.currency = currency;
        this.rate = rate;
    }
}
