package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.signUp.SignUpModel;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class LoginUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    LoginUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> loginUser(String emailId, String password, String deviceToken) {
        return tipsGoApiService.loginUser(emailId,password,deviceToken);

    }

    public Single<JsonElement> socialLogin(SignUpModel signUpModel) {
        return tipsGoApiService.socialLogin(signUpModel);
    }
}