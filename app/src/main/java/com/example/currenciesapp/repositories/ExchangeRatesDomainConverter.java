package com.example.currenciesapp.repositories;

import androidx.annotation.NonNull;

import com.example.currenciesapp.domain.ExchangeRate;
import com.example.currenciesapp.network.NetworkRates;
import com.example.currenciesapp.room.RoomExchangeRate;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ExchangeRatesDomainConverter {

    @Inject
    public ExchangeRatesDomainConverter() {

    }

    @NotNull
    public List<RoomExchangeRate> convertFromNetworkToRoom(NetworkRates networkRates) {
        String base = networkRates.base;
        Map<String, Double> rates = networkRates.rates;
        List<RoomExchangeRate> roomExchangeRateList = new ArrayList<>();
        for (String key : rates.keySet()) {
            roomExchangeRateList.add(
                    new RoomExchangeRate(key, rates.get(key), base));
        }
        return roomExchangeRateList;
    }

    @NonNull
    public List<ExchangeRate> convertFromNetworkToDomain(NetworkRates networkRates) {
        Map<String, Double> rates = networkRates.rates;
        String base = networkRates.base;
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for (String key : rates.keySet()) {
            exchangeRates.add(new ExchangeRate(base,
                    Currency.getInstance(key), rates.get(key)));
        }
        return exchangeRates;
    }

    @NonNull
    public List<ExchangeRate> convertFromRoomToDomain(List<RoomExchangeRate> roomExchangeRates) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for (RoomExchangeRate roomRate : roomExchangeRates) {
            exchangeRates.add(new ExchangeRate(roomRate.base,
                    Currency.getInstance(roomRate.currencyCode), roomRate.exchangeRate));
        }
        return exchangeRates;
    }
}
