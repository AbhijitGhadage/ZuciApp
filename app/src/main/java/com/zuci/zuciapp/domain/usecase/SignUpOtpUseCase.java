package com.zuci.zuciapp.domain.usecase;

import com.zuci.zuciapp.network.TipsGoApiService;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Single;

public class SignUpOtpUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    SignUpOtpUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<JsonElement> otpVerifyRegister(int registerIdInt, String emailId, String otp) {
        return tipsGoApiService.otpVerifyRegister(registerIdInt,emailId,otp);
    }



    public Single<JsonElement> resendOtpMailId(String emailId) {
        return tipsGoApiService.forgotPassword(emailId);

    }

}
