package com.zuci.zuciapp.ui.agoraVoiceCall;

import com.zuci.zuciapp.utils.AppResponse;
import com.zuci.zuciapp.network.TipsGoApiService;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class VoiceCallUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    VoiceCallUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<AppResponse> updateCallStatus(long CallId) {
        return tipsGoApiService.updateCallStatus(CallId);
    }

    public Single<JsonElement> deductsCoinsCalls(VideoAudioCutCoinsModel videoAudioCutCoinsModel) {
        return tipsGoApiService.deductsCoinsCalls(videoAudioCutCoinsModel);
    }
}
