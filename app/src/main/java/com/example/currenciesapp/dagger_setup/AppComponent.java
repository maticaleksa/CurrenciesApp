package com.example.currenciesapp.dagger_setup;

import android.app.Application;

import com.example.currenciesapp.CurrenciesApplication;
import com.example.currenciesapp.repositories.ExchangeRateRepositoryDaggerModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ActivityBuildersModule.class,
        NetworkModule.class, RoomDatabaseModule.class, ExchangeRateRepositoryDaggerModule.class})
public interface AppComponent extends AndroidInjector<CurrenciesApplication> {

    void inject(CurrenciesApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder bindApplication(Application app);

        AppComponent build();
    }
}
