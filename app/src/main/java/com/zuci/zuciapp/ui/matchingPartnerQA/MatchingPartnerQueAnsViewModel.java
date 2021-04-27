package com.zuci.zuciapp.ui.matchingPartnerQA;

import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.ui.base.BaseViewModel;
import com.zuci.zuciapp.utils.AppResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MatchingPartnerQueAnsViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MatchingPartnerUseCase matchingPartnerUseCase;
    private final MutableLiveData<List<QueAnsModel>> queAnsModelList = new MutableLiveData<>();
    private final MutableLiveData<AppResponse> partnerQueAnsList = new MutableLiveData<>();

    @Inject
    public MatchingPartnerQueAnsViewModel(MatchingPartnerUseCase matchingPartnerUseCase) {
        this.matchingPartnerUseCase = matchingPartnerUseCase;
    }

    public MutableLiveData<List<QueAnsModel>> getQueAnsModelList() {
        showLoading(true);
        disposables.add(matchingPartnerUseCase.getQueAnsModelList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    showLoading(false);
                    queAnsModelList.setValue(data);
                }, error -> {
                    showLoading(false);
                    queAnsModelList.setValue(null);
                }));
        return queAnsModelList;
    }

    public MutableLiveData<AppResponse> addPartnerQueAnsList(List<MatchingPartnerQueAnsModel> partnerQueAnsModels) {
        showLoading(true);
        disposables.add(matchingPartnerUseCase.addMatchingPartnerQueAnsUseCase(partnerQueAnsModels)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    showLoading(false);
                    partnerQueAnsList.setValue(data);
                }, error -> {
                    showLoading(false);
                    partnerQueAnsList.setValue(null);
                }));
        return partnerQueAnsList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
