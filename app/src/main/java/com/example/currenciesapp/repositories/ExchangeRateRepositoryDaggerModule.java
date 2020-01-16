package com.example.currenciesapp.repositories;

import com.example.currenciesapp.general.TimeUnitWithAmount;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ExchangeRateRepositoryDaggerModule {

    @Singleton
    @Provides
    public static QueryFrequency provideQueryFrequency() {
        return new QueryFrequency(new TimeUnitWithAmount(TimeUnit.SECONDS, 1));
    }
}
