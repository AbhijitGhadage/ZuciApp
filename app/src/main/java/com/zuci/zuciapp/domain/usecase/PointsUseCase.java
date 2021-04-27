package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.transaction.PointsAddCoinsModel;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class PointsUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    PointsUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> addCoinsApi(PointsAddCoinsModel pointsAddCoinsModel) {
        return tipsGoApiService.addCoins(pointsAddCoinsModel);

    }

}
