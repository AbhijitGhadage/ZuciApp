package com.zuci.zuciapp.ui.homeViewDetails.tabFragment;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.HomeImageUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TabViewModel extends BaseViewModel {
    private final HomeImageUseCase homeImageUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> mediaListResponse = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> mediaVideoListResponse = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> deductedImageRes = new MutableLiveData<>();

    @Inject
    TabViewModel(HomeImageUseCase homeImageUseCase) {
        this.homeImageUseCase = homeImageUseCase;
    }

    MutableLiveData<ApiResponse> getMediaIMGList(int registerId, int LoginRegistrationId) {
        showLoading(true);
        disposables.add(homeImageUseCase.galleryListApi(registerId, ConstantApp.IMAGE, LoginRegistrationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> mediaListResponse.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    mediaListResponse.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    mediaListResponse.setValue(ApiResponse.error(error));
                }));
        return mediaListResponse;
    }

    MutableLiveData<ApiResponse> ResponseHomeImageList() {
        return mediaListResponse;
    }


    MutableLiveData<ApiResponse> getMediaVideoList(int registerId, int LoginRegistrationId) {
        showLoading(true);
        disposables.add(homeImageUseCase.galleryListApi(registerId, ConstantApp.VIDEO, LoginRegistrationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> mediaVideoListResponse.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    mediaVideoListResponse.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    mediaVideoListResponse.setValue(ApiResponse.error(error));
                }));
        return mediaVideoListResponse;
    }

    MutableLiveData<ApiResponse> getVideoListResponse() {
        return mediaVideoListResponse;
    }



    public void deductionCoinsApi(DeductionModel DeductionModel) {
        showLoading(true);
        disposables.add(homeImageUseCase.deductionCoins(DeductionModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> deductedImageRes.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    deductedImageRes.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    deductedImageRes.setValue(ApiResponse.error(error));
                }));
    }

    MutableLiveData<ApiResponse> getDeductedImgResponse() {
        return deductedImageRes;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
