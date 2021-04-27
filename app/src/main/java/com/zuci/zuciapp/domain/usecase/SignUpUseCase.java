package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;

import javax.inject.Inject;

public class SignUpUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    SignUpUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

/*    public Single<JsonElement> signUpUser() {
        return tipsGoApiService.signUpUser();

    }*/

}
