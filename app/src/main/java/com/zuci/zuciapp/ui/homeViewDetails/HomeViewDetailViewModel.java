package com.zuci.zuciapp.ui.homeViewDetails;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.HomeDetailUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewDetailViewModel extends BaseViewModel {
    private final HomeDetailUseCase homeDetailUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<CallResponse> voiceCallResponse = new MutableLiveData<>();
    private final MutableLiveData<CallResponse> videoCallResponse = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> audioCallRes = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> videoCallRes = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> followMutableLiveData = new MutableLiveData<>();

    @Inject
    HomeViewDetailViewModel(HomeDetailUseCase homeDetailUseCase) {
        this.homeDetailUseCase = homeDetailUseCase;
    }

    void getVoiceCallResponse(int senderId, int receiverId, Activity activity) {
        showLoading(true);
        disposables.add(homeDetailUseCase.getHomeDetailsCallApi(senderId, receiverId, ConstantApp.AUDIO_CALL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> voiceCallResponse.setValue(null))
                .subscribe(data -> {
                    showLoading(false);
                    ((HomeViewDetailActivity) activity).openVoiceCall(data, ConstantApp.CALLER);

                }, error -> {
                    showLoading(false);
                    voiceCallResponse.setValue(null);
                }));
    }

    MutableLiveData<CallResponse> setCallAudioResponse() {
        return voiceCallResponse;
    }

    void getVideoCallResponse(int senderId, Integer receiverId, Activity activity) {
        showLoading(true);
        disposables.add(homeDetailUseCase.getHomeDetailsCallApi(senderId, receiverId, ConstantApp.VIDEO_CALL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> voiceCallResponse.setValue(null))
                .subscribe(data -> {
                    showLoading(false);
                    ((HomeViewDetailActivity) activity).openVideoCall(data, ConstantApp.CALLER);
                }, error -> {
                    showLoading(false);
                    voiceCallResponse.setValue(null);
                }));
    }

    MutableLiveData<CallResponse> setCallVideoResponse() {
        return voiceCallResponse;
    }


    public void getTotalCoinsAudio(Integer registerId) {
        showLoading(true);
        disposables.add(homeDetailUseCase.totalCoins(registerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> audioCallRes.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    audioCallRes.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    audioCallRes.setValue(ApiResponse.error(error));
                }));
    }

    MutableLiveData<ApiResponse> getTotalCoinsACResponse() {
        return audioCallRes;
    }


    public void getTotalCoinsVideo(Integer registerId) {
        showLoading(true);
        disposables.add(homeDetailUseCase.totalCoins(registerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> videoCallRes.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    videoCallRes.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    videoCallRes.setValue(ApiResponse.error(error));
                }));
    }

    MutableLiveData<ApiResponse> getTotalCoinsVCResponse() {
        return videoCallRes;
    }

    public void setFollow(long registerId,long followRegisterId) {
        showLoading(true);
        disposables.add(homeDetailUseCase.follow(registerId,followRegisterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> followMutableLiveData.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    followMutableLiveData.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    followMutableLiveData.setValue(ApiResponse.error(error));
                }));
    }

    MutableLiveData<ApiResponse> getFollowResponse() {
        return followMutableLiveData;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

}