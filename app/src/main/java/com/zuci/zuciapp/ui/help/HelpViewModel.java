package com.zuci.zuciapp.ui.help;

import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class HelpViewModel extends BaseViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    HelpViewModel(){
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}