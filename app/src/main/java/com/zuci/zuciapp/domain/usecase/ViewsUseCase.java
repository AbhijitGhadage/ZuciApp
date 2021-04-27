package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class ViewsUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    ViewsUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> updateMediaStatus(Integer registerId, int videoId, String sltPublicPri) {
        return tipsGoApiService.updateMediaStatus(registerId,videoId,sltPublicPri);
    }

    public Single<JsonElement> videoViewsApi(long registerId,long MediaRegId, long MediaId) {
        return tipsGoApiService.videoViews(registerId,MediaRegId,MediaId);
    }

}
