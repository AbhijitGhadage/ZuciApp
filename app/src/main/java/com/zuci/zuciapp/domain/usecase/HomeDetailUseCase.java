package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class HomeDetailUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    HomeDetailUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<CallResponse> getHomeDetailsCallApi(int SenderUserId, int ReceiverUserId, String callType) {
        return tipsGoApiService.getCallResponseApi(SenderUserId, ReceiverUserId, callType);
    }

    public Single<JsonElement> totalCoins(Integer registerId) {
        return tipsGoApiService.totalCoins(registerId);
    }

    public Single<JsonElement> follow(long registerId,long followRegisterId) {
        return tipsGoApiService.followApi(registerId,followRegisterId);
    }
}
