package com.zuci.zuciapp.ui.transaction;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.AddCoinsRateUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddCoinsRateViewModel extends BaseViewModel {
    private final AddCoinsRateUseCase addCoinsRateUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    @Inject
    AddCoinsRateViewModel(AddCoinsRateUseCase addCoinsRateUseCase){
        this.addCoinsRateUseCase = addCoinsRateUseCase;
    }

    public void coinsRatesApi(AddCoinsRateModel addCoinsRateModel) {
        showLoading(true);
        disposables.add(addCoinsRateUseCase.coinsApi(addCoinsRateModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> apiResponse.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    apiResponse.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    apiResponse.setValue(ApiResponse.error(error));
                }));
    }

    MutableLiveData<ApiResponse> getCoinsResponse() {
        return apiResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }


}
