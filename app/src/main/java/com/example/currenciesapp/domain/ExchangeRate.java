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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExchangeRate that = (ExchangeRate) o;

        if (base != null ? !base.equals(that.base) : that.base != null) return false;
        return currency != null ? currency.equals(that.currency) : that.currency == null;
    }

    @Override
    public int hashCode() {
        int result = base != null ? base.hashCode() : 0;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }
}
