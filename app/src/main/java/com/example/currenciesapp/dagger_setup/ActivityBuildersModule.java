package com.example.currenciesapp.dagger_setup;

import com.example.currenciesapp.currencies_screen.CurrenciesActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = {CurrenciesFragmentBuildersModule.class})
    public abstract CurrenciesActivity contributesCurrenciesActivity();
}
