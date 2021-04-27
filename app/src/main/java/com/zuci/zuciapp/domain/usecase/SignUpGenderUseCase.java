package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.signUp.SignUpModel;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class SignUpGenderUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    SignUpGenderUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> signUpUser(SignUpModel signUpModel) {
        return tipsGoApiService.signUpUser(signUpModel);
    }
}
