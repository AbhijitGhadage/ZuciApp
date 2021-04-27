package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SettingUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    SettingUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> updateProfileImg(RequestBody userId,  MultipartBody.Part imageName) {
        return tipsGoApiService.uploadProfileImg(userId,imageName);
    }

}

