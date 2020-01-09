package com.example.currenciesapp.network;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Data class that is used to store results from the network currency query.
 */
public class NetworkRates {

    @SerializedName("base")
    public final String base;

    @SerializedName("rates")
    public final Map<String, Double> rates;

    public NetworkRates(String base, Map<String, Double> rates) {
        this.base = base;
        this.rates = rates;
    }
}
