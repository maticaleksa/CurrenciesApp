package com.example.currenciesapp.dagger_setup;

import com.example.currenciesapp.currencies_screen.CurrenciesFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class CurrenciesFragmentBuildersModule {

    @ContributesAndroidInjector
    public abstract CurrenciesFragment contributesCurrenciesFragment();
}
