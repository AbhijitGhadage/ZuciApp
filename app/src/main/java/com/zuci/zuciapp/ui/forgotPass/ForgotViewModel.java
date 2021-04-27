package com.zuci.zuciapp.ui.forgotPass;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.ForgotUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ForgotViewModel  extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final ForgotUseCase forgotUseCase;
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    @Inject
    public ForgotViewModel(ForgotUseCase forgotUseCase) {
        this.forgotUseCase = forgotUseCase;
    }

    void forgotPasswordMailId(String emailId) {
        showLoading(true);
        disposables.add(forgotUseCase.forgotPasswordMailId(emailId)
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

    MutableLiveData<ApiResponse> getForgotPasswordResponse() {
        return apiResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

}