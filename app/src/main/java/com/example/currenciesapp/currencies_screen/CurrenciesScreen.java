package com.example.currenciesapp.currencies_screen;

import java.util.List;

public interface CurrenciesScreen {

    void displayCurrencies(List<ExchangeRatesViewModel> rates);
    void displayError(ErrorType type);
}
