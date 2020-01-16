package com.example.currenciesapp;


import com.example.currenciesapp.domain.ExchangeRate;
import com.example.currenciesapp.errors.NoInternetException;
import com.example.currenciesapp.errors.UnknownNetworkException;
import com.example.currenciesapp.general.Result;
import com.example.currenciesapp.general.TimeUnitWithAmount;
import com.example.currenciesapp.network.NetworkRates;
import com.example.currenciesapp.network.NetworkRatesSource;
import com.example.currenciesapp.network_checking.NetworkConnectivityNotifier;
import com.example.currenciesapp.repositories.ExchangeRatesDomainConverter;
import com.example.currenciesapp.repositories.ExchangeRatesRepository;
import com.example.currenciesapp.repositories.QueryFrequency;
import com.example.currenciesapp.room.RoomExchangeRate;
import com.example.currenciesapp.room.RoomExchangeRateDao;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;

import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ExchangeRatesRepositoryTest {

    private RoomExchangeRateDao dao;
    private NetworkRatesSource networkRatesSource;
    private NetworkConnectivityNotifier networkConnectivityNotifier;
    private QueryFrequency queryFrequency;
    private ExchangeRatesDomainConverter domainConverter;
    private ExchangeRatesRepository exchangeRatesRepository;

    private BehaviorSubject<List<RoomExchangeRate>> daoEmitter = BehaviorSubject.create();
    private PublishSubject<Boolean> connectionStatusEmitter = PublishSubject.create();

    @Rule
    public SchedulersRule schedulersRule = new SchedulersRule();

    @Before
    public void setUp() {
        domainConverter = new ExchangeRatesDomainConverter();
        queryFrequency = new QueryFrequency(new TimeUnitWithAmount(TimeUnit.SECONDS, 1));
        networkConnectivityNotifier = mock(NetworkConnectivityNotifier.class);
    }

    @Test
    public void testRepositoryEmitsProperlyWhenDatabaseIsEmpty() throws IOException {
        when(networkConnectivityNotifier.observeConnectionStatus()).thenReturn(Observable.just(true));
        setupDao();
        Call<NetworkRates> call = getSuccessfulNetworkRatesCall();
        networkRatesSource = mock(NetworkRatesSource.class);
        when(networkRatesSource.getRates()).thenReturn(call);
        exchangeRatesRepository = new ExchangeRatesRepository(dao, networkRatesSource,
                networkConnectivityNotifier, domainConverter, queryFrequency);

        TestObserver<Result<List<ExchangeRate>>> testObserver = exchangeRatesRepository.observeRates().test();
        schedulersRule.getTestScheduler().advanceTimeBy(queryFrequency.frequency.amount, queryFrequency.frequency.timeUnit);

        Result<List<ExchangeRate>> expectedValue = Result.success(domainConverter.convertFromNetworkToDomain(getNetworkRates()));
        testObserver.assertValue(expectedValue);
    }

    @Test
    public void testRepositoryEmitsProperlyWhenNetworkAndDatabaseEmitProperly() throws IOException {
        when(networkConnectivityNotifier.observeConnectionStatus()).thenReturn(Observable.just(true));
        daoEmitter.onNext(getRoomExchangeRateList());
        setupDao();
        Call<NetworkRates> call = getSuccessfulNetworkRatesCall();
        networkRatesSource = mock(NetworkRatesSource.class);
        when(networkRatesSource.getRates()).thenReturn(call);
        exchangeRatesRepository = new ExchangeRatesRepository(dao, networkRatesSource,
                networkConnectivityNotifier, domainConverter, queryFrequency);

        TestObserver<Result<List<ExchangeRate>>> testObserver = exchangeRatesRepository.observeRates().test();
        schedulersRule.getTestScheduler().advanceTimeBy(queryFrequency.frequency.amount, queryFrequency.frequency.timeUnit);

        Result<List<ExchangeRate>> expectedValue = Result.success(domainConverter.convertFromNetworkToDomain(getNetworkRates()));
        testObserver.assertValues(expectedValue, expectedValue);
    }

    @Test
    public void testRepositoryEmitsResultNoNetworkExceptionWhenThereIsNoNetwork() throws IOException {
        when(networkConnectivityNotifier.observeConnectionStatus()).thenReturn(Observable.just(false));
        daoEmitter.onNext(getRoomExchangeRateList());
        setupDao();
        Call<NetworkRates> call = getSuccessfulNetworkRatesCall();
        networkRatesSource = mock(NetworkRatesSource.class);
        when(networkRatesSource.getRates()).thenReturn(call);
        exchangeRatesRepository = new ExchangeRatesRepository(dao, networkRatesSource,
                networkConnectivityNotifier, domainConverter, queryFrequency);

        TestObserver<Result<List<ExchangeRate>>> testObserver = exchangeRatesRepository.observeRates().test();
        schedulersRule.getTestScheduler().advanceTimeBy(queryFrequency.frequency.amount, queryFrequency.frequency.timeUnit);

        Result<List<ExchangeRate>> successResult = Result.success(domainConverter.convertFromNetworkToDomain(getNetworkRates()));
        Result<List<ExchangeRate>> errResult = Result.error(new NoInternetException());
        List<Result<List<ExchangeRate>>> expectedResults = new ArrayList<>();
        expectedResults.add(successResult);
        expectedResults.add(errResult);
        testObserver.assertValueSet(expectedResults);
    }

    @Test
    public void testRepositoryEmitsProperResultWhenNetworkWasAbsentButThenReturns() throws IOException {
        when(networkConnectivityNotifier.observeConnectionStatus()).thenReturn(connectionStatusEmitter);
        setupDao();
        Call<NetworkRates> call = getSuccessfulNetworkRatesCall();
        networkRatesSource = mock(NetworkRatesSource.class);
        when(networkRatesSource.getRates()).thenReturn(call);
        exchangeRatesRepository = new ExchangeRatesRepository(dao, networkRatesSource,
                networkConnectivityNotifier, domainConverter, queryFrequency);

        TestObserver<Result<List<ExchangeRate>>> testObserver = exchangeRatesRepository.observeRates().test();
        connectionStatusEmitter.onNext(false);
        connectionStatusEmitter.onNext(true);
        schedulersRule.getTestScheduler().advanceTimeBy(queryFrequency.frequency.amount, queryFrequency.frequency.timeUnit);

        Result<List<ExchangeRate>> successResult = Result.success(domainConverter.convertFromNetworkToDomain(getNetworkRates()));
        Result<List<ExchangeRate>> errResult = Result.error(new NoInternetException());
        List<Result<List<ExchangeRate>>> expectedResults = new ArrayList<>();
        expectedResults.add(successResult);
        expectedResults.add(errResult);
        testObserver.assertValues(errResult, successResult);
    }

    @Test
    public void testRepositoryEmitsProperResultWhenNetworkWasPresentThenAbsentThenPresentAgain() throws IOException {
        when(networkConnectivityNotifier.observeConnectionStatus()).thenReturn(connectionStatusEmitter);
        setupDao();
        Call<NetworkRates> call = getSuccessfulNetworkRatesCall();
        networkRatesSource = mock(NetworkRatesSource.class);
        when(networkRatesSource.getRates()).thenReturn(call);
        exchangeRatesRepository = new ExchangeRatesRepository(dao, networkRatesSource,
                networkConnectivityNotifier, domainConverter, queryFrequency);

        TestObserver<Result<List<ExchangeRate>>> testObserver = exchangeRatesRepository.observeRates().test();
        connectionStatusEmitter.onNext(true);
        schedulersRule.getTestScheduler().advanceTimeBy(queryFrequency.frequency.amount, queryFrequency.frequency.timeUnit);
        connectionStatusEmitter.onNext(false);
        connectionStatusEmitter.onNext(true);
        schedulersRule.getTestScheduler().advanceTimeBy(queryFrequency.frequency.amount, queryFrequency.frequency.timeUnit);

        Result<List<ExchangeRate>> successResult = Result.success(domainConverter.convertFromNetworkToDomain(getNetworkRates()));
        Result<List<ExchangeRate>> errResult = Result.error(new NoInternetException());
        List<Result<List<ExchangeRate>>> expectedResults = new ArrayList<>();
        expectedResults.add(successResult);
        expectedResults.add(errResult);
        testObserver.assertValues(successResult, errResult, successResult);
    }

    @Test
    public void testRepositoryEmitsResultUnknownNetworkExceptionWhenNetworkRequestFails() throws IOException {
        when(networkConnectivityNotifier.observeConnectionStatus()).thenReturn(Observable.just(true));
        daoEmitter.onNext(getRoomExchangeRateList());
        setupDao();
        Call<NetworkRates> call = getUnSuccessfulNetworkRatesCall();
        networkRatesSource = mock(NetworkRatesSource.class);
        when(networkRatesSource.getRates()).thenReturn(call);
        exchangeRatesRepository = new ExchangeRatesRepository(dao, networkRatesSource,
                networkConnectivityNotifier, domainConverter, queryFrequency);

        TestObserver<Result<List<ExchangeRate>>> testObserver = exchangeRatesRepository.observeRates().test();
        schedulersRule.getTestScheduler().advanceTimeBy(queryFrequency.frequency.amount, queryFrequency.frequency.timeUnit);

        Result<List<ExchangeRate>> successResult = Result.success(domainConverter.convertFromNetworkToDomain(getNetworkRates()));
        Result<List<ExchangeRate>> errResult = Result.error(new UnknownNetworkException("Response.error()"));
        List<Result<List<ExchangeRate>>> expectedResults = new ArrayList<>();
        expectedResults.add(successResult);
        expectedResults.add(errResult);
        testObserver.assertValues(successResult, errResult);
    }

    @Test
    public void testRepositoryEmitsRequiredNumberOfTimesDuringATimePeriod() throws IOException {
        when(networkConnectivityNotifier.observeConnectionStatus()).thenReturn(Observable.just(true));
        setupDao();
        Call<NetworkRates> call = getSuccessfulNetworkRatesCall();
        networkRatesSource = mock(NetworkRatesSource.class);
        when(networkRatesSource.getRates()).thenReturn(call);
        exchangeRatesRepository = new ExchangeRatesRepository(dao, networkRatesSource,
                networkConnectivityNotifier, domainConverter, queryFrequency);

        TestObserver<Result<List<ExchangeRate>>> testObserver = exchangeRatesRepository.observeRates().test();
        schedulersRule.getTestScheduler().advanceTimeBy(queryFrequency.frequency.amount, queryFrequency.frequency.timeUnit);
        schedulersRule.getTestScheduler().advanceTimeBy(queryFrequency.frequency.amount, queryFrequency.frequency.timeUnit);
        schedulersRule.getTestScheduler().advanceTimeBy(queryFrequency.frequency.amount, queryFrequency.frequency.timeUnit);
        schedulersRule.getTestScheduler().advanceTimeBy(queryFrequency.frequency.amount, queryFrequency.frequency.timeUnit);

        Result<List<ExchangeRate>> expectedValue = Result.success(domainConverter.convertFromNetworkToDomain(getNetworkRates()));
        testObserver.assertValueCount(4);
    }

    @NotNull
    private Call<NetworkRates> getSuccessfulNetworkRatesCall() throws IOException {
        Call<NetworkRates> call = mock(Call.class);
        NetworkRates networkRates = getNetworkRates();
        Response response = Response.success(networkRates);
        when(call.execute()).thenReturn(response);
        return call;
    }

    @NotNull
    private Call<NetworkRates> getUnSuccessfulNetworkRatesCall() throws IOException {
        Call<NetworkRates> call = mock(Call.class);
        ResponseBody responseBody = getFailureResponseBody();
        Response response = Response.error(403, responseBody);
        when(call.execute()).thenReturn(response);
        return call;
    }

    @NotNull
    private ResponseBody getFailureResponseBody() {
        return ResponseBody.create(MediaType.parse("application/json"), "error");
    }

    @NotNull
    private NetworkRates getNetworkRates() {
        Map<String, Double> ratesMap = new HashMap();
        ratesMap.put("EUR", (double) 1);
        return new NetworkRates("EUR", ratesMap);
    }

    private void setupDao() {
        dao = mock(RoomExchangeRateDao.class);
        List<RoomExchangeRate> roomList = getRoomExchangeRateList();
        when(dao.getAll()).thenReturn(daoEmitter.toFlowable(BackpressureStrategy.DROP));
        doAnswer(invocation -> {
            daoEmitter.onNext(roomList);
            return invocation;
        }).when(dao).insert(roomList);
    }

    @NotNull
    private List<RoomExchangeRate> getRoomExchangeRateList() {
        return Collections.singletonList(new RoomExchangeRate("EUR", 1, "EUR"));
    }

}