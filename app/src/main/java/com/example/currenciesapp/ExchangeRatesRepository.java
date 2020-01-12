package com.example.currenciesapp;

import com.example.currenciesapp.domain.ExchangeRate;
import com.example.currenciesapp.errors.NoInternetException;
import com.example.currenciesapp.network.NetworkRates;
import com.example.currenciesapp.network.NetworkRatesSource;
import com.example.currenciesapp.network_checking.NetworkConnectivityNotifier;
import com.example.currenciesapp.errors.UnknownNetworkException;
import com.example.currenciesapp.room.RoomExchangeRate;
import com.example.currenciesapp.room.RoomExchangeRateDao;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Repository for ExchangeRates.
 */
public class ExchangeRatesRepository {

    private final RoomExchangeRateDao dao;
    private final NetworkRatesSource networkRatesSource;
    private final NetworkConnectivityNotifier networkConnectivityNotifier;

    @Inject
    public ExchangeRatesRepository(RoomExchangeRateDao dao,
                                   NetworkRatesSource networkRatesSource,
                                   NetworkConnectivityNotifier networkConnectivityNotifier) {
        this.dao = dao;
        this.networkRatesSource = networkRatesSource;
        this.networkConnectivityNotifier = networkConnectivityNotifier;
    }

    /**
     * Exposes a stream that emits List<ExchangeRate> from the database.
     * The network api is queried every second which updates the database
     * (causing new emissions every 1 second).
     */
    public Observable<Result<List<ExchangeRate>>> observeRates() {
        return getFromDb()
                .mergeWith(
                        networkConnectivityNotifier.observeConnectionStatus()
                                .switchMap(isConnected -> {
                                            if (isConnected) {
                                                return Observable.interval(1, TimeUnit.SECONDS)
                                                        .switchMap(_a -> getFromNetwork());

                                            } else {
                                                return Observable.just(Result.error(new NoInternetException()));
                                            }
                                        }
                                ));
    }

    private Observable<Result<List<ExchangeRate>>> getFromNetwork() {
        return Observable.fromCallable(() -> {
            Response<NetworkRates> response = networkRatesSource.getRates().execute();
            if (!response.isSuccessful()) {
                return Result.<List<ExchangeRate>>error(new UnknownNetworkException(response.message()));
            }
            NetworkRates networkRates = response.body();
            String base = networkRates.base;
            Map<String, Double> rates = networkRates.rates;
            List<RoomExchangeRate> roomExchangeRateList = new ArrayList<>();
            for (String key : rates.keySet()) {
                roomExchangeRateList.add(
                        new RoomExchangeRate(key, rates.get(key), base));
            }
            dao.insert(roomExchangeRateList);
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            for (String key : rates.keySet()) {
                exchangeRates.add(new ExchangeRate(base,
                        Currency.getInstance(key), rates.get(key)));
            }
            return Result.success(exchangeRates);
        })
                .filter(exchangeRates -> false);
    }

    private Observable<Result<List<ExchangeRate>>> getFromDb() {
        return dao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(roomExchangeRates -> {
                    List<ExchangeRate> exchangeRates = new ArrayList<>();
                    for (RoomExchangeRate roomRate : roomExchangeRates) {
                        exchangeRates.add(new ExchangeRate(roomRate.base,
                                Currency.getInstance(roomRate.currencyCode), roomRate.exchangeRate));
                    }
                    return exchangeRates;
                })
                .map(Result::success)
                .toObservable();
    }
}
