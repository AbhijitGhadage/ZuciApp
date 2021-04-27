package com.zuci.zuciapp.ui.signUp;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.SignUpGenderUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SignUpGenderViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final SignUpGenderUseCase genderUseCase;
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    @Inject
    public SignUpGenderViewModel(SignUpGenderUseCase genderUseCase) {
        this.genderUseCase = genderUseCase;
    }


    void signUpUser(SignUpModel signUpModel) {
        showLoading(true);
        disposables.add(genderUseCase.signUpUser(signUpModel)
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


    MutableLiveData<ApiResponse> getSignUpResponse() {
        return apiResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }


}
