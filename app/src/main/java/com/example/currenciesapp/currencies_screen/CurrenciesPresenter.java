package com.example.currenciesapp.currencies_screen;

import android.util.Log;

import com.example.currenciesapp.ExchangeRatesRepository;
import com.example.currenciesapp.domain.ExchangeRate;
import com.example.currenciesapp.errors.NoInternetException;
import com.example.currenciesapp.errors.UnknownNetworkException;
import com.example.currenciesapp.general.BasePresenter;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.inject.Inject;

public class CurrenciesPresenter extends BasePresenter {

    private final ExchangeRatesRepository repository;

    @Inject
    public CurrenciesPresenter(ExchangeRatesRepository repository) {
        this.repository = repository;
    }

    void onDisplay(CurrenciesScreen screen) {
        addDisposable(repository.observeRates()
                .subscribe(listResult -> {
                            if (listResult.success() && listResult.getData().size() != 0) {
                                Currency baseCurrency = Currency.getInstance(listResult.getData().get(0).base);
                                List<ExchangeRatesViewModel> list = new ArrayList<>();
                                list.add(new ExchangeRatesViewModel(baseCurrency.getCurrencyCode(), baseCurrency.getDisplayName(), 1));
                                for (ExchangeRate rate : listResult.getData()) {
                                    list.add(new ExchangeRatesViewModel(rate.currency.getCurrencyCode(),
                                            rate.currency.getDisplayName(), rate.rate));
                                }
                                screen.displayCurrencies(list);
                            } else {
                                if (!listResult.success()) {
                                    if (listResult.getError() instanceof NoInternetException)
                                        screen.displayError(ErrorType.NO_INTERNET);
                                    else if (listResult.getError() instanceof UnknownNetworkException)
                                        screen.displayError(ErrorType.UNKNOWN_NETWORK_ERROR);
                                    else
                                        screen.displayError(ErrorType.UNKNOWN);
                                }
                            }
                        },
                        throwable -> {
                            // TODO: 1/10/2020
                            Log.e("ERR", throwable.getMessage());
                        }));
    }

}
