package com.zuci.zuciapp.ui.signUp;

import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SignUpViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public SignUpViewModel() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

}