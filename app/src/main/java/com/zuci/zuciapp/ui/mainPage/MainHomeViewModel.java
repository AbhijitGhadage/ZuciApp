package com.zuci.zuciapp.ui.mainPage;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.PointsUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.transaction.PointsAddCoinsModel;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainHomeViewModel extends BaseViewModel {
    private final PointsUseCase pointsUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    @Inject
    MainHomeViewModel(PointsUseCase pointsUseCase){
        this.pointsUseCase=pointsUseCase;
    }

    public void addCoinsApi(PointsAddCoinsModel pointsAddCoinsModel) {
        showLoading(true);
        disposables.add(pointsUseCase.addCoinsApi(pointsAddCoinsModel)
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