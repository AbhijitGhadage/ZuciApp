package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.homeViewDetails.tabFragment.DeductionModel;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class HomeImageUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    HomeImageUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> galleryListApi(int registerId, String image,int LoginRegistrationId) {
        return tipsGoApiService.getGalleryList(registerId, image,LoginRegistrationId);
    }

    public Single<JsonElement> deductionCoins(DeductionModel deductionModel) {
        return tipsGoApiService.deductsCoins(deductionModel);
    }

}
