package com.zuci.zuciapp.ui.splash;


import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.SplashUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SplashViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final SplashUseCase splashUseCase;
    private final MutableLiveData<ApiResponse> responseMutableLiveData = new MutableLiveData<>();

    @Inject
    SplashViewModel(SplashUseCase splashUseCase){
        this.splashUseCase=splashUseCase;
    }

    public void getTotalCoins(Integer registerId) {
//        showLoading(true);
            disposables.add(splashUseCase.totalCoins(registerId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(data -> responseMutableLiveData.setValue(ApiResponse.loading()))
                    .subscribe(data -> {
                        showLoading(false);
                        responseMutableLiveData.setValue(ApiResponse.success(data));
                    }, error -> {
                        showLoading(false);
                        responseMutableLiveData.setValue(ApiResponse.error(error));
                    }));
        }

        MutableLiveData<ApiResponse> getTotalCoinsResponse() {
            return responseMutableLiveData;
        }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }


}
