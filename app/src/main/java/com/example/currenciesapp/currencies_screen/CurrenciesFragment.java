package com.example.currenciesapp.currencies_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currenciesapp.ErrorDialogFragment;
import com.example.currenciesapp.R;
import com.example.currenciesapp.general.BaseFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * Displays a list of currencies with their respective rates.
 */
public class CurrenciesFragment extends BaseFragment implements CurrenciesScreen, CurrenciesListAdapter.ItemClickListener {

    private RecyclerView list;
    private CurrenciesListAdapter adapter;

    @Inject
    CurrenciesPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_currencies, container, false);
        adapter = new CurrenciesListAdapter(this);
        list = v.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        setPresenter(presenter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onDisplay(this);
    }

    @Override
    public void displayCurrencies(List<ExchangeRatesViewModel> rates) {
        adapter.setList(rates);
    }

    @Override
    public void displayError(ErrorType type) {
        ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
        switch (type) {
            case NO_INTERNET:
                errorDialogFragment.displayDialog(getString(R.string.no_internet_stale_rates), getFragmentManager());
                break;
            case UNKNOWN_NETWORK_ERROR:
                errorDialogFragment.displayDialog(getString(R.string.unknown_error_stale_rates), getFragmentManager());
        }
    }

    @Override
    public void onItemClick() {
        list.scrollToPosition(0);
    }
}
