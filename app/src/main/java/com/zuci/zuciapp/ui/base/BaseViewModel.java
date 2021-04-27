package com.zuci.zuciapp.ui.base;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public abstract class BaseViewModel extends ViewModel {
    @NonNull
    private final MutableLiveData<Boolean> loadingIndicator;

    protected BaseViewModel() {
        loadingIndicator = new MutableLiveData<>();
    }

    @NonNull
    LiveData<Boolean> getLoadingIndicator() {
        return loadingIndicator;
    }

    protected void showLoading(boolean showLoading) {
        this.loadingIndicator.setValue(showLoading);
    }

}
