package com.zuci.zuciapp.ui.agoraVoiceCall;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.utils.AppResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VoiceCallViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final VoiceCallUseCase voiceCallUseCase;
    private final MutableLiveData<AppResponse> changeCallStaus = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    @Inject
    public VoiceCallViewModel(VoiceCallUseCase voiceCallUseCase) {
        this.voiceCallUseCase = voiceCallUseCase;
    }

    MutableLiveData<AppResponse> getChangeCallStaus(long callId) {
        showLoading(true);
        disposables.add(voiceCallUseCase.updateCallStatus(callId)
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
        disposables.add(voiceCallUseCase.deductsCoinsCalls(videoAudioCutCoinsModel)
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
