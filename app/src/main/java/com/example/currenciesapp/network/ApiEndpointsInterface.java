package com.example.currenciesapp.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit api calls.
 */
public interface ApiEndpointsInterface {

    @GET("latest?base=EUR")
    Call<NetworkRates> getRates();
}
