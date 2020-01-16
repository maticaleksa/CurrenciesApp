package com.example.currenciesapp.currencies_screen;

import com.github.nitrico.lastadapter.StableId;

/**
 * Data class that is used to display currencies.
 */
public class ExchangeRatesViewModel implements StableId {
    public final String currencyCode;
    public final String currency;
    public final double amount;

    public ExchangeRatesViewModel(String currencyCode, String currency, double amount) {
        this.currencyCode = currencyCode;
        this.currency = currency;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExchangeRatesViewModel that = (ExchangeRatesViewModel) o;

        if (currencyCode != null ? !currencyCode.equals(that.currencyCode) : that.currencyCode != null)
            return false;
        return currency != null ? currency.equals(that.currency) : that.currency == null;
    }

    @Override
    public int hashCode() {
        int result = currencyCode != null ? currencyCode.hashCode() : 0;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    @Override
    public long getStableId() {
        return (long)hashCode();
    }
}
