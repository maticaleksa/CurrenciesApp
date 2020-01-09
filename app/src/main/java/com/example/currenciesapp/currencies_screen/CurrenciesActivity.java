package com.example.currenciesapp.currencies_screen;


import android.os.Bundle;

import com.example.currenciesapp.R;

import dagger.android.support.DaggerAppCompatActivity;

public class CurrenciesActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
