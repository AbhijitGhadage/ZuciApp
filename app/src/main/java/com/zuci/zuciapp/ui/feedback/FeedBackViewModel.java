package com.zuci.zuciapp.ui.feedback;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.FeedbackUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FeedBackViewModel extends BaseViewModel {
    private final FeedbackUseCase feedbackUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    @Inject
    FeedBackViewModel(FeedbackUseCase feedbackUseCase){
        this.feedbackUseCase=feedbackUseCase;
    }

    void feedback(FeedbackModel feedbackModel) {
        showLoading(true);
        disposables.add(feedbackUseCase.feedback(feedbackModel)
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

    MutableLiveData<ApiResponse> getFeedbackResponse() {
        return apiResponse;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}