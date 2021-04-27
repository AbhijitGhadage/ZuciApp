package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.google.gson.JsonElement;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GalleryVideoUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    GalleryVideoUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> uploadGalleryImg(Map<String, RequestBody> imageUploadData, MultipartBody.Part imageName) {
        return tipsGoApiService.uploadGalleryImg(imageUploadData, imageName);
    }

    public Single<JsonElement> galleryListApi(int registerId, String image,int LoginRegistrationId) {
        return tipsGoApiService.getGalleryList(registerId, image,LoginRegistrationId);
    }

    public Single<JsonElement> galleryDeleteImageApi(long imageId) {
        return tipsGoApiService.deleteImage(imageId);
    }
}
