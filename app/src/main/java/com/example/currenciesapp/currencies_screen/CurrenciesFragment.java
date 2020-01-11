package com.example.currenciesapp.currencies_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currenciesapp.R;
import com.example.currenciesapp.general.BaseFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * Displays a list of currencies with their respective rates.
 */
public class CurrenciesFragment extends BaseFragment implements CurrenciesScreen {

    private RecyclerView list;
    private CurrenciesListAdapter adapter;

    @Inject
    CurrenciesPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_currencies, container, false);
        adapter = new CurrenciesListAdapter();
        list = v.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
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
}
