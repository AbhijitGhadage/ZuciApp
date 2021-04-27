package com.zuci.zuciapp.ui.reels;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.ReelsVideoUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.DeductionModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ReelsVideoViewModel  extends BaseViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final ReelsVideoUseCase reelsVideoUseCase;
    private final MutableLiveData<ApiResponse> responseMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> responseLikes = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> viewsResponse = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> deductedImageRes = new MutableLiveData<>();

    @Inject
    ReelsVideoViewModel(ReelsVideoUseCase reelsVideoUseCase) {
        this.reelsVideoUseCase = reelsVideoUseCase;
    }

    MutableLiveData<ApiResponse> getMediaList(int registerId) {
        showLoading(true);
        disposables.add(reelsVideoUseCase.galleryListApi(registerId)
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
        return responseMutableLiveData;
    }

    MutableLiveData<ApiResponse> getGalleryUploadVideoListResponse() {
        return responseMutableLiveData;
    }

    public void addLinksApi(VideoAddLikeModel videoAddLikeModel) {
//        showLoading(true);
        disposables.add(reelsVideoUseCase.addLinksApi(videoAddLikeModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> responseLikes.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    responseLikes.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    responseLikes.setValue(ApiResponse.error(error));
                }));
    }

    MutableLiveData<ApiResponse> getLikesResponse() {
        return responseLikes;
    }


    public void setViewsCount(long registerId,long MediaRegId, long MediaId) {
//        showLoading(true);
        disposables.add(reelsVideoUseCase.videoViewsApi(registerId,MediaRegId,MediaId)
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



    public void deductionCoinsApi(DeductionModel DeductionModel) {
        showLoading(true);
        disposables.add(reelsVideoUseCase.deductionCoins(DeductionModel)
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
