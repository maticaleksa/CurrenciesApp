package com.example.currenciesapp.errors;

public class UnknownNetworkException extends Exception {
    public UnknownNetworkException(String msg) {
        super("server returned: " + msg);
    }
}
