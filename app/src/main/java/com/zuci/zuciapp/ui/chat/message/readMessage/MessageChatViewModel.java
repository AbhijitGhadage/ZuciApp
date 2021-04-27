package com.zuci.zuciapp.ui.chat.message.readMessage;

import com.zuci.zuciapp.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MessageChatViewModel extends BaseViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    MessageChatViewModel() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
