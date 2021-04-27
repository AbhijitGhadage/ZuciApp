package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.agoraLive.LiveCallResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class HomeListUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    HomeListUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> getHomePageList(Integer userId, Integer pageNo, Integer typeId) {
        return tipsGoApiService.getHomePageList(userId, pageNo, typeId);
    }

    public Single<CallResponse> getCallResponseData(int SenderUserId, int ReceiverUserId, String callType) {
        return tipsGoApiService.getCallResponseApi(SenderUserId, ReceiverUserId, callType);
    }

    public Single<JsonElement> totalCoins(Integer registerId) {
        return tipsGoApiService.totalCoins(registerId);
    }

    public Single<LiveCallResponse> liveCallResponseApi(long RegistrationId) {
        return tipsGoApiService.liveCallResponseApi(RegistrationId);
    }
}
