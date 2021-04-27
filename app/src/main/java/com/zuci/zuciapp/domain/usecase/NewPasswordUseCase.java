package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class NewPasswordUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    NewPasswordUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> newPassword(int registerIdInt,String emailId,String password) {
        return tipsGoApiService.setNewPassword(registerIdInt,emailId,password);

    }
}
