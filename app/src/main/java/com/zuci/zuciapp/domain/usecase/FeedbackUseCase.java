package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.feedback.FeedbackModel;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class FeedbackUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    FeedbackUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> feedback(FeedbackModel feedbackModel) {
        return tipsGoApiService.feedback(feedbackModel);

    }

}
