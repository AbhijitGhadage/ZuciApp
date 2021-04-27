package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class TransactionUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    TransactionUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> transactionLog(Integer registerId) {
        return tipsGoApiService.transactionLog(registerId);
    }
}
