package com.zuci.zuciapp.ui.home;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.HomeListUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.zuci.zuciapp.ui.base.BaseViewModel;
import com.zuci.zuciapp.ui.mainPage.MainHomeActivity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends BaseViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final HomeListUseCase homeListUseCase;
    private final MutableLiveData<ApiResponse> nearByRestaurant = new MutableLiveData<>();
    private final MutableLiveData<CallResponse> voiceCallResponse = new MutableLiveData<>();
    private final MutableLiveData<CallResponse> videoCallResponse = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> responseMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<CoinsResponse> coinsMutableLiveData = new MutableLiveData<>();

    @Inject
    HomeViewModel(HomeListUseCase homeListUseCase) {
        this.homeListUseCase = homeListUseCase;
    }

    void getHomePageList(Integer userId, Integer pageNo, Integer typeId) {
        showLoading(true);
        disposables.add(homeListUseCase.getHomePageList(userId, pageNo, typeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> nearByRestaurant.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    nearByRestaurant.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    nearByRestaurant.setValue(ApiResponse.error(error));
                }));
    }

    MutableLiveData<ApiResponse> setHomePageList() {
        return nearByRestaurant;
    }

    void getVoiceCallResponse(int senderId, int receiverId, Activity activity) {
        showLoading(true);
        disposables.add(homeListUseCase.getCallResponseData(senderId, receiverId, ConstantApp.AUDIO_CALL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> voiceCallResponse.setValue(null))
                .subscribe(data -> {
                    showLoading(false);
                    ((MainHomeActivity) activity).openVoiceCall(data, ConstantApp.CALLER);
                }, error -> {
                    showLoading(false);
                    voiceCallResponse.setValue(null);
                }));
    }

    MutableLiveData<CallResponse> setCallResponse() {
        return voiceCallResponse;
    }

    void getVideoCallResponse(int senderId, Integer receiverId, Activity activity) {
        showLoading(true);
        disposables.add(homeListUseCase.getCallResponseData(senderId, receiverId, ConstantApp.VIDEO_CALL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> voiceCallResponse.setValue(null))
                .subscribe(data -> {
                    showLoading(false);
                    ((MainHomeActivity) activity).openVideoCall(data, ConstantApp.CALLER);
                }, error -> {
                    showLoading(false);
                    voiceCallResponse.setValue(null);
                }));
    }

    void getLiveCallResponse(int RegistrationId, Activity activity) {
        showLoading(true);
        disposables.add(homeListUseCase.liveCallResponseApi(RegistrationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> voiceCallResponse.setValue(null))
                .subscribe(data -> {
                    showLoading(false);
                    ((MainHomeActivity) activity).openLiveCall(data);
                }, error -> {
                    showLoading(false);
                    voiceCallResponse.setValue(null);
                }));
    }


    /*public void getTotalCoins(Integer registerId) {
        showLoading(true);
        disposables.add(homeListUseCase.totalCoins(registerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> responseMutableLiveData.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    responseMutableLiveData.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    responseMutableLiveData.setValue(ApiResponse.error(error));
                }));
    }

    MutableLiveData<ApiResponse> getTotalCoinsResponse() {
        return responseMutableLiveData;
    }
*/
    public MutableLiveData<ApiResponse> getTotalCoins(Integer registerId) {
        showLoading(true);
        disposables.add(homeListUseCase.totalCoins(registerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> responseMutableLiveData.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    responseMutableLiveData.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    responseMutableLiveData.setValue(ApiResponse.error(error));
                }));
        return responseMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
