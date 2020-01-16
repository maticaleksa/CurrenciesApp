package com.example.currenciesapp.general;

import java.util.concurrent.TimeUnit;

public class TimeUnitWithAmount {
    public final TimeUnit timeUnit;
    public final long amount;

    public TimeUnitWithAmount(TimeUnit timeUnit, long amount) {
        this.timeUnit = timeUnit;
        this.amount = amount;
    }
}
