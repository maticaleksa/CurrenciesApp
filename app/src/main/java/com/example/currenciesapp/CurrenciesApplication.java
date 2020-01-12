package com.example.currenciesapp;

import android.util.Log;

import com.example.currenciesapp.dagger_setup.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.reactivex.plugins.RxJavaPlugins;

public class CurrenciesApplication extends DaggerApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        RxJavaPlugins.setErrorHandler(throwable -> {
            Log.e("ERR", throwable.getMessage());
        });
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().bindApplication(this).build();
    }
}
