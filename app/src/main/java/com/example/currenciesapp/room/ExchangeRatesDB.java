package com.example.currenciesapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RoomExchangeRate.class}, version = 1)
public abstract class ExchangeRatesDB extends RoomDatabase {

    public abstract RoomExchangeRateDao exchangeRateDao();
}
