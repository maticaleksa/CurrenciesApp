package com.example.currenciesapp.dagger_setup;

import android.app.Application;

import androidx.room.Room;

import com.example.currenciesapp.room.ExchangeRatesDB;
import com.example.currenciesapp.room.RoomExchangeRateDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomDatabaseModule {

    @Singleton
    @Provides
    public static ExchangeRatesDB provideDB(Application app) {
        return Room.databaseBuilder(app, ExchangeRatesDB.class, "exchange_rates_db")
                .build();
    }

    @Singleton
    @Provides
    public static RoomExchangeRateDao provideRoomExchangeRateDao(ExchangeRatesDB db) {
        return db.exchangeRateDao();
    }
}
