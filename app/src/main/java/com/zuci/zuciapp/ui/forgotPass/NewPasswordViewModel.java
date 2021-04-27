package com.zuci.zuciapp.ui.forgotPass;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.NewPasswordUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NewPasswordViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final NewPasswordUseCase newPasswordUseCase;
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    @Inject
    public NewPasswordViewModel(NewPasswordUseCase newPasswordUseCase) {
        this.newPasswordUseCase = newPasswordUseCase;
    }

    void newPassword(int registerIdInt,String emailId,String password) {
        showLoading(true);
        disposables.add(newPasswordUseCase.newPassword(registerIdInt,emailId,password)
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

    MutableLiveData<ApiResponse> getNewPasswordResponse() {
        return apiResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

}