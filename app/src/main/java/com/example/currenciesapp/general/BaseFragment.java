package com.example.currenciesapp.general;

import dagger.android.support.DaggerFragment;

/**
 * Base class for all fragments that have an accompanying presenter.
 * Handles the destruction of all disposables contained in the before mentioned presenter.
 */
public class BaseFragment extends DaggerFragment {

    private BasePresenter presenter;

    public void setPresenter(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.removeDisposables();
        }
        super.onDestroy();
    }
}
