package com.example.currenciesapp.usecases;

import com.example.currenciesapp.domain.ExchangeRate;
import com.example.currenciesapp.general.Result;
import com.example.currenciesapp.repositories.ExchangeRatesRepository;

import java.util.Currency;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetExchangeRatesUseCase {

    private final ExchangeRatesRepository repository;

    @Inject
    public GetExchangeRatesUseCase(ExchangeRatesRepository repository) {
        this.repository = repository;
    }

    public Observable<Result<List<ExchangeRate>>> observeRates() {
        return repository.observeRates()
                .map(listResult -> {
                    if (!listResult.success() || listResult.getData().size() == 0) return listResult;
                    List<ExchangeRate> list = listResult.getData();
                    // add the base currency
                    Currency baseCurrency = Currency.getInstance(list.get(0).base);
                    list.add(0, new ExchangeRate(baseCurrency.getCurrencyCode(), baseCurrency, 1));
                    return Result.success(list);
                });
    }
}
