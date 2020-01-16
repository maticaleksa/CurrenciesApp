package com.example.currenciesapp.currencies_screen;

import com.example.currenciesapp.domain.Money;

import java.util.List;

public interface CurrenciesScreen {

    void displayCurrencies(List<ExchangeRatesViewModel> rates);
    void displayError(ErrorType type);
    Money getInsertedMoneyAmount();

}
