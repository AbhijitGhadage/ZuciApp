package com.zuci.zuciapp.ui.matchingPartnerQA;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.utils.AppResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class MatchingPartnerUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    MatchingPartnerUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<List<QueAnsModel>> getQueAnsModelList() {
        return tipsGoApiService.getQueAnsListApi();
    }

    public Single<AppResponse> addMatchingPartnerQueAnsUseCase(List<MatchingPartnerQueAnsModel> partnerQueAnsModels) {
        return tipsGoApiService.addMatchingPartnerQueAnsApi(partnerQueAnsModels);
    }
}
