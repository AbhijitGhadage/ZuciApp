package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.zuci.zuciapp.ui.transaction.CashWithdrawalModel;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class CashWithdrawalUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    CashWithdrawalUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> cashWithdrawalApi(CashWithdrawalModel cashWithdrawalModel) {
        return tipsGoApiService.cashWithdrawal(cashWithdrawalModel);

    }
}
