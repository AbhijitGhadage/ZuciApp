package com.zuci.zuciapp.ui.agoraVideoCall;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.VideoAudioCutCoinsModel;
import com.zuci.zuciapp.utils.AppResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VideoCallViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final VideoCallUseCase videoCallUseCase;
    private final MutableLiveData<AppResponse> changeCallStaus = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    @Inject
    public VideoCallViewModel(VideoCallUseCase videoCallUseCase) {
        this.videoCallUseCase = videoCallUseCase;
    }

    MutableLiveData<AppResponse> getChangeCallStaus(long callId) {
        showLoading(true);
        disposables.add(videoCallUseCase.updateCallStatus(callId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    showLoading(false);
                    changeCallStaus.setValue(data);
                }, error -> {
                    showLoading(false);
                    changeCallStaus.setValue(null);
                }));
        return changeCallStaus;
    }

    public void deductsCoinsCallsApi(VideoAudioCutCoinsModel videoAudioCutCoinsModel) {
        showLoading(true);
        disposables.add(videoCallUseCase.deductsCoinsCalls(videoAudioCutCoinsModel)
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

    MutableLiveData<ApiResponse> getCallsResponse() {
        return apiResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}

