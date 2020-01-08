package com.example.currenciesapp.currencies_screen;

/**
 * Data class that is used to display currencies.
 */
public class CurrencyViewModel {
    public final String currencyShort;
    public final String currency;

    public CurrencyViewModel(String currencyShort, String currency) {
        this.currencyShort = currencyShort;
        this.currency = currency;
    }
}
