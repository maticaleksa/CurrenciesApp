package com.example.currenciesapp.dagger_setup;


import com.example.currenciesapp.network.ApiEndpointsInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Singleton
    @Provides
    public static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .cache(null)
                .build();
    }

    @Singleton
    @Provides
    public static Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://revolut.duckdns.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    public static ApiEndpointsInterface provideApiEndpointInterface(Retrofit retrofit) {
        return retrofit.create(ApiEndpointsInterface.class);
    }
}
