package com.example.currenciesapp.network;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit api calls.
 */
public interface NetworkRatesSource {

    @GET("latest?base=EUR")
    Call<NetworkRates> getRates();
}
