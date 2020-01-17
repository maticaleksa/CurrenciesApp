package com.example.currenciesapp.repositories;

import com.example.currenciesapp.domain.ExchangeRate;
import com.example.currenciesapp.errors.NoInternetException;
import com.example.currenciesapp.general.Result;
import com.example.currenciesapp.network.NetworkRates;
import com.example.currenciesapp.network.NetworkRatesSource;
import com.example.currenciesapp.network_checking.NetworkConnectivityNotifier;
import com.example.currenciesapp.errors.UnknownNetworkException;
import com.example.currenciesapp.room.RoomExchangeRate;
import com.example.currenciesapp.room.RoomExchangeRateDao;

import java.util.Currency;
import java.util.List;

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
    private final ExchangeRatesDomainConverter domainConverter;
    /**
     * Defines how often network should be queried.
     */
    private final QueryFrequency queryFrequency;

    @Inject
    public ExchangeRatesRepository(RoomExchangeRateDao dao,
                                   NetworkRatesSource networkRatesSource,
                                   NetworkConnectivityNotifier networkConnectivityNotifier,
                                   ExchangeRatesDomainConverter domainConverter,
                                   QueryFrequency queryFrequency) {
        this.dao = dao;
        this.networkRatesSource = networkRatesSource;
        this.networkConnectivityNotifier = networkConnectivityNotifier;
        this.domainConverter = domainConverter;
        this.queryFrequency = queryFrequency;
    }

    /**
     * Exposes a stream that emits List<ExchangeRate> from the database.
     * The network api is queried every specified amount of time which updates the database
     * (causing new emissions every specified amount of time).
     */
    public Observable<Result<List<ExchangeRate>>> observeRates() {
        return getFromDb()
                .mergeWith(
                        networkConnectivityNotifier.observeConnectionStatus()
                                .switchMap(isConnected -> {
                                            if (isConnected) {
                                                return queryFrequency.getIntervalObservable()
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

            List<RoomExchangeRate> roomExchangeRateList = domainConverter.convertFromNetworkToRoom(networkRates);
            dao.insert(roomExchangeRateList);
            return Result.success(domainConverter.convertFromNetworkToDomain(networkRates));
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .filter(result -> !result.success());
    }


    private Observable<Result<List<ExchangeRate>>> getFromDb() {
        return dao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(domainConverter::convertFromRoomToDomain)
                .map(Result::success)
                .toObservable();
    }
}
