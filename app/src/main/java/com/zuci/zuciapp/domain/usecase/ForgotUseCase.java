package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class ForgotUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    ForgotUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> forgotPasswordMailId(String emailId) {
        return tipsGoApiService.forgotPassword(emailId);

    }

}
