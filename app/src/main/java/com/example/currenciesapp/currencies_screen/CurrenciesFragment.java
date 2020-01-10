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

import dagger.android.support.DaggerFragment;

/**
 * Displays a list of currencies with their respective rates.
 */
public class CurrenciesFragment extends DaggerFragment {

    private RecyclerView list;
    private CurrenciesListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_currencies, container, false);
        adapter = new CurrenciesListAdapter();
        list = v.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        list.setAdapter(adapter);
        return v;
    }


}
