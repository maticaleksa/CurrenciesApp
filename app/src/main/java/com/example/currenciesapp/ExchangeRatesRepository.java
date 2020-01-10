package com.example.currenciesapp;

import com.example.currenciesapp.domain.ExchangeRate;
import com.example.currenciesapp.network.NetworkRatesSource;
import com.example.currenciesapp.room.RoomExchangeRate;
import com.example.currenciesapp.room.RoomExchangeRateDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Repository for ExchangeRates.
 */
public class ExchangeRatesRepository {

    private final RoomExchangeRateDao dao;
    private final NetworkRatesSource networkRatesSource;

    @Inject
    public ExchangeRatesRepository(RoomExchangeRateDao dao, NetworkRatesSource networkRatesSource) {
        this.dao = dao;
        this.networkRatesSource = networkRatesSource;
    }

    /**
     * Exposes a stream that emits List<ExchangeRate> from the database.
     * The network api is queried every second which updates the database
     * (causing new emissions every 1 second).
     */
    public Observable<Result<List<ExchangeRate>>> observeRates() {
        return getFromDb()
                .mergeWith(
                        Observable.interval(1, TimeUnit.SECONDS)
                                .switchMap(_a -> getFromNetwork()))
                .map(Result::success);
    }

    private Observable<List<ExchangeRate>> getFromNetwork() {
        return Observable.fromCallable(() -> networkRatesSource.getRates().execute())
                .doOnNext(networkRatesResponse -> {
                    if (networkRatesResponse.isSuccessful()) {
                        String base = networkRatesResponse.body().base;
                        Map<String, Double> rates = networkRatesResponse.body().rates;
                        List<RoomExchangeRate> roomExchangeRateList = new ArrayList<>();
                        for (String key : rates.keySet()) {
                            roomExchangeRateList.add(
                                    new RoomExchangeRate(key, rates.get(key), base));
                        }
                        dao.insert(roomExchangeRateList);
                    } else {
                        // TODO: 1/10/2020 handle all errors
                    }
                })
                .map(networkRatesResponse -> {
                    Map<String, Double> rates = networkRatesResponse.body().rates;
                    List<ExchangeRate> exchangeRates = new ArrayList<>();
                    for (String key : rates.keySet()) {
                        exchangeRates.add(new ExchangeRate(key, rates.get(key)));
                    }
                    return exchangeRates;
                })
                .filter(exchangeRates -> false)
                .subscribeOn(Schedulers.io());

    }

    private Observable<List<ExchangeRate>> getFromDb() {
        return dao.getAll()
                .map(roomExchangeRates -> {
                    List<ExchangeRate> exchangeRates = new ArrayList<>();
                    for (RoomExchangeRate roomRate : roomExchangeRates) {
                        exchangeRates.add(new ExchangeRate(roomRate.currencyCode, roomRate.exchangeRate));
                    }
                    return exchangeRates;
                }).toObservable();
    }
}
