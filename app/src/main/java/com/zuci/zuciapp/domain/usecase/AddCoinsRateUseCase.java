package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.transaction.AddCoinsRateModel;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class AddCoinsRateUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    AddCoinsRateUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> coinsApi(AddCoinsRateModel addCoinsRateModel) {
        return tipsGoApiService.coinsRatesApi(addCoinsRateModel);

    }
}
