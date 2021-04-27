package com.zuci.zuciapp.ui.changePass;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.ChangePasswordUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswordViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final ChangePasswordUseCase changePasswordUseCase;
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    @Inject
    ChangePasswordViewModel(ChangePasswordUseCase changePasswordUseCase){
        this.changePasswordUseCase=changePasswordUseCase;
    }

    void changePassword(String emailId, String oldPass, String oldNewPass) {
        showLoading(true);
        disposables.add(changePasswordUseCase.changePassword(emailId,oldPass,oldNewPass)
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

    MutableLiveData<ApiResponse> getChangePassResponse() {
        return apiResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}