package com.example.currenciesapp.errors;

public class NoInternetException extends Exception {
    public NoInternetException() {
        super("Device is not connected to the internet");
    }
}
