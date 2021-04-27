package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class ForgotOTPUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    ForgotOTPUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> forgotPasswordOTPVerify(int registerIdInt,String emailId,String otp) {
        return tipsGoApiService.otpVerifyRegister(registerIdInt,emailId,otp);

    }

    public Single<JsonElement> resendOtpMailId(String emailId) {
        return tipsGoApiService.forgotPassword(emailId);

    }
}
