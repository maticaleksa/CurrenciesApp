package com.example.currenciesapp.general;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Base class for presenters. Contains logic for handling disposables.
 */
public abstract class BasePresenter {

    private CompositeDisposable compositeDisposable;

    public void addDisposable (Disposable d) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(d);
    }

    public void removeDisposables() {
        compositeDisposable.clear();
    }
}
