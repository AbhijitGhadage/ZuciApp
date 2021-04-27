package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class SplashUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    SplashUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> totalCoins(Integer registerId) {
        return tipsGoApiService.totalCoins(registerId);
    }
}
