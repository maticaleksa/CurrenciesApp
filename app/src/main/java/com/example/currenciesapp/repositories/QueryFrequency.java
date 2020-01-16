package com.example.currenciesapp.repositories;


import com.example.currenciesapp.general.TimeUnitWithAmount;

import io.reactivex.Observable;

public class QueryFrequency {

    public final TimeUnitWithAmount frequency;

    public QueryFrequency(TimeUnitWithAmount frequency) {
        this.frequency = frequency;
    }

    /**
     * Returns an Observable that emits a Long every specified interval of time.
     */
    public Observable<Long> getIntervalObservable() {
        return Observable.interval(frequency.amount, frequency.timeUnit);
    }
}
