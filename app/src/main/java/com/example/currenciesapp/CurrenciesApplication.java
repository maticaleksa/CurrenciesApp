package com.example.currenciesapp;

import com.example.currenciesapp.dagger_setup.DaggerAppComponent;


import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class CurrenciesApplication extends DaggerApplication {


    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().bindApplication(this).build();
    }
}
