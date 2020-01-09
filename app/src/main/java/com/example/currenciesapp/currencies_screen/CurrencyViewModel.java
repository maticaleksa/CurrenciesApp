package com.example.currenciesapp.currencies_screen;

/**
 * Data class that is used to display currencies.
 */
public class CurrencyViewModel {
    public final String currencyCode;
    public final String currency;

    public CurrencyViewModel(String currencyCode, String currency) {
        this.currencyCode = currencyCode;
        this.currency = currency;
    }
}
