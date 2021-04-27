package com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory;

import com.zuci.zuciapp.network.TipsGoApiService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class MessageListUseCase {
    private final TipsGoApiService tipsGoApiService;

    @Inject
    MessageListUseCase(TipsGoApiService tipsGoApiService) {
        this.tipsGoApiService = tipsGoApiService;
    }

    public Single<List<ChatListModel>> getChatUserList(long UserId) {
        return tipsGoApiService.getChatUserList(UserId);
    }
}
