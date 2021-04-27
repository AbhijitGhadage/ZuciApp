package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.DeductionModel;
import com.zuci.zuciapp.ui.reels.VideoAddLikeModel;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class ReelsVideoUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    ReelsVideoUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> galleryListApi(int registerId) {
        return tipsGoApiService.getVideoList(registerId);
    }

    public Single<JsonElement> addLinksApi(VideoAddLikeModel videoAddLikeModel) {
        return tipsGoApiService.getLikesApi(videoAddLikeModel);
    }

    public Single<JsonElement> videoViewsApi(long registerId, long MediaRegId, long MediaId) {
        return tipsGoApiService.videoViews(registerId,MediaRegId,MediaId);
    }

    public Single<JsonElement> deductionCoins(DeductionModel deductionModel) {
        return tipsGoApiService.deductsCoins(deductionModel);
    }
}
