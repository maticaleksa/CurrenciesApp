package com.example.currenciesapp.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exchange_rates")
public class RoomExchangeRate {

    @PrimaryKey
    public final String currencyCode;

    public final double exchangeRate;

    public final String base;

    public RoomExchangeRate(String currencyCode, double exchangeRate, String base) {
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate;
        this.base = base;
    }
}
