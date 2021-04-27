package com.zuci.zuciapp.ui.transaction;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.CashWithdrawalUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CashWithdrawalViewModel  extends BaseViewModel {
    private final CashWithdrawalUseCase feedbackUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    @Inject
    CashWithdrawalViewModel(CashWithdrawalUseCase feedbackUseCase){
        this.feedbackUseCase=feedbackUseCase;
    }

    void cashWithdrawalApi(CashWithdrawalModel cashWithdrawalModel) {
        showLoading(true);
        disposables.add(feedbackUseCase.cashWithdrawalApi(cashWithdrawalModel)
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

    MutableLiveData<ApiResponse> getCashWithdrawalResponse() {
        return apiResponse;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}