package com.zuci.zuciapp.di.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import dagger.Lazy;

public class ViewModelFactory<VM extends ViewModel> implements ViewModelProvider.Factory {
    private Lazy<VM> viewModel;

    @Inject
    public ViewModelFactory(Lazy<VM> viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    @NonNull
    @SuppressWarnings("unchecked cast")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) viewModel.get();
    }
}
