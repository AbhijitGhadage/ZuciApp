package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.signUp.SignUpModel;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileCreateUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    ProfileCreateUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> profileCreate(SignUpModel signUpModel) {
        return tipsGoApiService.profileCreate(signUpModel);

    }

    public Single<JsonElement> countryList() {
        return tipsGoApiService.countryList();
    }


    public Single<JsonElement> updateProfileImg(RequestBody userId, MultipartBody.Part imageName) {
        return tipsGoApiService.uploadProfileImg(userId,imageName);
    }
}
