package com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.zuci.zuciapp.ui.base.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MessageListViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MessageListUseCase messageListUseCase;
    private MutableLiveData<List<ChatListModel>> chatListModelMutableLiveData = new MediatorLiveData<>();

    @Inject
    MessageListViewModel(MessageListUseCase messageListUseCase) {
        this.messageListUseCase = messageListUseCase;
    }

    MutableLiveData<List<ChatListModel>> getChatUserList(long UserId) {
        showLoading(true);
        disposables.add(messageListUseCase.getChatUserList(UserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    showLoading(false);
                    chatListModelMutableLiveData.setValue(data);
                }, error -> {
                    showLoading(false);
                    chatListModelMutableLiveData.setValue(null);
                }));
        return chatListModelMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}