package com.zuci.zuciapp.ui.gallery;


import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.GalleryUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import java.io.File;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GalleryViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final GalleryUseCase galleryUseCase;
    private final MutableLiveData<ApiResponse> uploadMediaFile = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> mediaListResponse = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> deleteMediaResponse = new MutableLiveData<>();

    @Inject
    GalleryViewModel(GalleryUseCase galleryUseCase) {
        this.galleryUseCase = galleryUseCase;
    }

    void uploadImage(Map<String, RequestBody> imageUploadData, File file) {
        showLoading(true);
        MultipartBody.Part image = null;
        if (file != null) {
            RequestBody profile_image = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            image = MultipartBody.Part.createFormData("Name", file.getName(), profile_image);
        }
        disposables.add(galleryUseCase.uploadGalleryImg(imageUploadData, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> uploadMediaFile.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    uploadMediaFile.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    uploadMediaFile.setValue(ApiResponse.error(error));
                }));
    }

    MutableLiveData<ApiResponse> getGalleryUploadResponse() {
        return uploadMediaFile;
    }

    MutableLiveData<ApiResponse> getMediaList(int registerId) {
        showLoading(true);
        disposables.add(galleryUseCase.galleryListApi(registerId, ConstantApp.IMAGE,0)
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

    MutableLiveData<ApiResponse> deleteMediaFile(long imageId) {
        showLoading(true);
        disposables.add(galleryUseCase.galleryDeleteImageApi(imageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> deleteMediaResponse.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    deleteMediaResponse.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    deleteMediaResponse.setValue(ApiResponse.error(error));
                }));
        return deleteMediaResponse;
    }

    MutableLiveData<ApiResponse> getImageDeleteResponse() {
        return deleteMediaResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
