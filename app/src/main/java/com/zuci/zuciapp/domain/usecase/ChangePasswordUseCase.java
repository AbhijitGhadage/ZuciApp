package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class ChangePasswordUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    ChangePasswordUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> changePassword(String emailId, String oldPass, String oldNewPass) {
        return tipsGoApiService.changePassword(emailId,oldPass,oldNewPass);

    }


}
