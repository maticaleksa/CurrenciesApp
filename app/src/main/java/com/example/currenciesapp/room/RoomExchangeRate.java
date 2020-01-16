package com.example.currenciesapp.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exchange_rates")
public class RoomExchangeRate {

    @PrimaryKey
    @NonNull
    public final String currencyCode;

    public final double exchangeRate;

    public final String base;

    public RoomExchangeRate(String currencyCode, double exchangeRate, String base) {
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate;
        this.base = base;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomExchangeRate that = (RoomExchangeRate) o;

        if (Double.compare(that.exchangeRate, exchangeRate) != 0) return false;
        if (!currencyCode.equals(that.currencyCode)) return false;
        return base != null ? base.equals(that.base) : that.base == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = currencyCode.hashCode();
        temp = Double.doubleToLongBits(exchangeRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (base != null ? base.hashCode() : 0);
        return result;
    }
}
