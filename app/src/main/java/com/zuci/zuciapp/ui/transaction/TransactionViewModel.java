package com.zuci.zuciapp.ui.transaction;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.TransactionUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TransactionViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final TransactionUseCase transactionUseCase;
    private final MutableLiveData<ApiResponse> tranResMutableLiveData = new MutableLiveData<>();

    @Inject
    TransactionViewModel(TransactionUseCase transactionUseCase){
        this.transactionUseCase=transactionUseCase;
    }

    public void transactionList(Integer registerId) {
        showLoading(true);
        disposables.add(transactionUseCase.transactionLog(registerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> tranResMutableLiveData.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    tranResMutableLiveData.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    tranResMutableLiveData.setValue(ApiResponse.error(error));
                }));
    }

    MutableLiveData<ApiResponse> getTransactionListResponse() {
        return tranResMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
