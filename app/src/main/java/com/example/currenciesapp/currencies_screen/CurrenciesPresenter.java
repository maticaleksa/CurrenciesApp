package com.example.currenciesapp.currencies_screen;

import com.example.currenciesapp.domain.ExchangeRate;
import com.example.currenciesapp.domain.Money;
import com.example.currenciesapp.errors.NoInternetException;
import com.example.currenciesapp.errors.UnknownNetworkException;
import com.example.currenciesapp.general.BasePresenter;
import com.example.currenciesapp.usecases.GetExchangeRatesUseCase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CurrenciesPresenter extends BasePresenter {

    private final GetExchangeRatesUseCase useCase;
    private List<ExchangeRate> rates;
    private float multiplier = 1;

    @Inject
    public CurrenciesPresenter(GetExchangeRatesUseCase useCase) {
        this.useCase = useCase;
    }

    void onDisplay(CurrenciesScreen screen) {
        addDisposable(useCase.observeRates()
                .subscribe(listResult -> {
                            if (listResult.success() && listResult.getData().size() != 0) {
                                rates = listResult.getData();

                                List<ExchangeRatesViewModel> list = new ArrayList<>();
                                for (ExchangeRate rate : rates) {
                                    ExchangeRatesViewModel viewModel = new ExchangeRatesViewModel(rate.currency.getCurrencyCode(),
                                            rate.currency.getDisplayName(), rate.rate * multiplier);
                                    list.add(viewModel);
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
                        }
                ));
    }

    void onAmountChanged(CurrenciesScreen screen) {
        Money money = screen.getInsertedMoneyAmount();
        for (ExchangeRate rate : rates) {
            if (rate.currency.equals(money.currency)) {
                multiplier = money.amount.divide(new BigDecimal(rate.rate), 4, RoundingMode.CEILING).floatValue();
            }
        }
        List<ExchangeRatesViewModel> viewModelList = new ArrayList<>();
        for (ExchangeRate rate : rates) {
            viewModelList.add(new ExchangeRatesViewModel(rate.currency.getCurrencyCode(),
                    rate.currency.getDisplayName(), rate.rate * multiplier));
        }
        screen.displayCurrencies(viewModelList);

    }

}
