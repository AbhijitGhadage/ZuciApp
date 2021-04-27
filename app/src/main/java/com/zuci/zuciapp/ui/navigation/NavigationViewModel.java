package com.zuci.zuciapp.ui.navigation;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.SettingUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NavigationViewModel extends BaseViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final SettingUseCase settingUseCase;
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    @Inject
    NavigationViewModel(SettingUseCase settingUseCase) {
        this.settingUseCase=settingUseCase;

    }

    void uploadImage(int registerId, File imageFile) {
        showLoading(true);
        //creating request body for file
        String registerIdStr = String.valueOf(registerId);
        RequestBody reg_id = RequestBody.create(MediaType.parse("multipart/form-data"), registerIdStr);
        MultipartBody.Part image = null;
        if (imageFile != null) {
            RequestBody profile_image = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            image = MultipartBody.Part.createFormData("ProfileImage", imageFile.getName(), profile_image);
        }

        disposables.add(settingUseCase.updateProfileImg(reg_id, image)
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


    MutableLiveData<ApiResponse> getUpdateImgResponse() {
        return apiResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
