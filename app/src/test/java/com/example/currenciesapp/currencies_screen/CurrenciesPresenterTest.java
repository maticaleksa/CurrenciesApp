package com.example.currenciesapp.currencies_screen;

import com.example.currenciesapp.SchedulersRule;
import com.example.currenciesapp.domain.ExchangeRate;
import com.example.currenciesapp.errors.NoInternetException;
import com.example.currenciesapp.errors.UnknownNetworkException;
import com.example.currenciesapp.general.Result;
import com.example.currenciesapp.usecases.GetExchangeRatesUseCase;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class CurrenciesPresenterTest {

    private GetExchangeRatesUseCase useCase;
    private CurrenciesScreen screen;
    private CurrenciesPresenter presenter;
    private PublishSubject<Result<List<ExchangeRate>>> repositorySubject = PublishSubject.create();

    @Rule
    public SchedulersRule rule;

    @Test
    public void testCurrenciesAreDisplayedInOnDisplayWhenRepositoryReturnsProperly() {
        screen = spy(CurrenciesScreen.class);
        useCase = mock(GetExchangeRatesUseCase.class);
        List<ExchangeRate> exchangeRates = getExchangeRateList();
        when(useCase.observeRates()).thenReturn(Observable.just(Result.success(exchangeRates)));
        presenter = new CurrenciesPresenter(useCase);

        presenter.onDisplay(screen);

        List<ExchangeRatesViewModel> expectedViewModel = new ArrayList<>();
        ExchangeRate exchangeRate = getExchangeRate();
        expectedViewModel.add(
                new ExchangeRatesViewModel(exchangeRate.currency.getCurrencyCode(),
                        exchangeRate.currency.getDisplayName(), 1)
        );
        verify(screen, times(1)).displayCurrencies(expectedViewModel);
    }

    @Test
    public void testCurrenciesAreDisplayedEveryTimeRepositoryEmits() {
        screen = spy(CurrenciesScreen.class);
        useCase = mock(GetExchangeRatesUseCase.class);
        when(useCase.observeRates()).thenReturn(repositorySubject);
        presenter = new CurrenciesPresenter(useCase);

        presenter.onDisplay(screen);
        repositorySubject.onNext(Result.success(getExchangeRateList()));
        repositorySubject.onNext(Result.success(getExchangeRateList()));
        repositorySubject.onNext(Result.success(getExchangeRateList()));

        verify(screen, times(3)).displayCurrencies(any());
    }

    @Test
    public void testDisplayErrorForNoInternetExceptionIsDisplayed() {
        testErrorDisplay(ErrorType.NO_INTERNET, new NoInternetException());
    }

    @Test
    public void testDisplayErrorForUnknownNetworkExceptionIsDisplayed() {
        testErrorDisplay(ErrorType.UNKNOWN_NETWORK_ERROR, new UnknownNetworkException("msg"));
    }

    @Test
    public void testDisplayErrorForUnknownIsDisplayed() {
        testErrorDisplay(ErrorType.UNKNOWN, new Exception());
    }


    private void testErrorDisplay(ErrorType type, Exception e) {
        screen = spy(CurrenciesScreen.class);
        useCase = mock(GetExchangeRatesUseCase.class);
        when(useCase.observeRates()).thenReturn(Observable.just(Result.error(e)));
        presenter = new CurrenciesPresenter(useCase);

        presenter.onDisplay(screen);

        verify(screen, times(1)).displayError(type);
    }
    @NotNull
    private List<ExchangeRate> getExchangeRateList() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(getExchangeRate());
        return exchangeRates;
    }

    @NotNull
    private ExchangeRate getExchangeRate() {
        return new ExchangeRate("EUR", Currency.getInstance("EUR"), 1);
    }
}