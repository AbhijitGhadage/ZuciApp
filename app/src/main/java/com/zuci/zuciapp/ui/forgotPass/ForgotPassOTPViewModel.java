package com.zuci.zuciapp.ui.forgotPass;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.ForgotOTPUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ForgotPassOTPViewModel  extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final ForgotOTPUseCase forgotOTPUseCase;
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> resendOtpApiResponse = new MutableLiveData<>();

    @Inject
    public ForgotPassOTPViewModel(ForgotOTPUseCase forgotOTPUseCase) {
        this.forgotOTPUseCase = forgotOTPUseCase;
    }

    void forgotPasswordMailId(int registerIdInt,String emailId,String otp) {
        showLoading(true);
        disposables.add(forgotOTPUseCase.forgotPasswordOTPVerify(registerIdInt,emailId,otp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> apiResponse.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    apiResponse.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    apiResponse.setValue(ApiResponse.error(error));
                }));

    }

    MutableLiveData<ApiResponse> getForgotOTPResponse() {
        return apiResponse;
    }


    void resendOtpMailId(String emailId) {
        showLoading(true);
        disposables.add(forgotOTPUseCase.resendOtpMailId(emailId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> apiResponse.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    apiResponse.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    apiResponse.setValue(ApiResponse.error(error));
                }));

    }

    MutableLiveData<ApiResponse> getResendOtpResponse() {
        return resendOtpApiResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

}