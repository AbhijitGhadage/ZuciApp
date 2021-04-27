package com.zuci.zuciapp.ui.imgVideoView;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.ViewsUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewsViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final ViewsUseCase viewsUseCase;
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> viewsResponse = new MutableLiveData<>();

    @Inject
    ViewsViewModel(ViewsUseCase viewsUseCase) {
        this.viewsUseCase = viewsUseCase;
    }

    void updateStatus(Integer registerId, int videoId, String sltPublicPri) {
        showLoading(true);
        disposables.add(viewsUseCase.updateMediaStatus(registerId, videoId, sltPublicPri)
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

    MutableLiveData<ApiResponse> getViewsResponse() {
        return apiResponse;
    }


    public void setViewsCount(long registerId, long MediaRegId, long MediaId) {
//        showLoading(true);
        disposables.add(viewsUseCase.videoViewsApi(registerId, MediaRegId, MediaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> viewsResponse.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    viewsResponse.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    viewsResponse.setValue(ApiResponse.error(error));
                }));
    }

    MutableLiveData<ApiResponse> getViewsCountResponse() {
        return viewsResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}