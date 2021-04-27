package com.zuci.zuciapp.ui.profileCreate;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.domain.usecase.ProfileCreateUseCase;
import com.zuci.zuciapp.network.entities.ApiResponse;
import com.zuci.zuciapp.ui.base.BaseViewModel;
import com.zuci.zuciapp.ui.signUp.SignUpModel;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileCreateViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final ProfileCreateUseCase profileCreateUseCase;
    private final MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> apiResponse2 = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> apiResponse3 = new MutableLiveData<>();

    @Inject
    public ProfileCreateViewModel(ProfileCreateUseCase profileCreateUseCase) {
        this.profileCreateUseCase = profileCreateUseCase;
    }

    void profileCreateUser(SignUpModel signUpModel) {
        showLoading(true);
        disposables.add(profileCreateUseCase.profileCreate(signUpModel)
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



    void countryList() {
        showLoading(true);
        disposables.add(profileCreateUseCase.countryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> apiResponse2.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    apiResponse2.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    apiResponse2.setValue(ApiResponse.error(error));
                }));
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

        disposables.add(profileCreateUseCase.updateProfileImg(reg_id, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> apiResponse3.setValue(ApiResponse.loading()))
                .subscribe(data -> {
                    showLoading(false);
                    apiResponse3.setValue(ApiResponse.success(data));
                }, error -> {
                    showLoading(false);
                    apiResponse3.setValue(ApiResponse.error(error));
                }));
    }


    MutableLiveData<ApiResponse> getProfileCreatedResponse() {
        return apiResponse;
    }

    MutableLiveData<ApiResponse> getCountryListResponse() {
        return apiResponse2;
    }

    MutableLiveData<ApiResponse> getUpdateImgResponse() {
        return apiResponse3;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}