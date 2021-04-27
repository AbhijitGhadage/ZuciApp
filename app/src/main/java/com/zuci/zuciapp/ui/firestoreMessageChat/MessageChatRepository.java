package com.zuci.zuciapp.ui.firestoreMessageChat;

import com.zuci.zuciapp.firebase.FirestoreChildCallBack;

import java.util.Map;

public interface MessageChatRepository {
    long getChatRoomId(long senderRegId, long receiverRegId);

    void getChatHistoryByChatRoomId(long senderRegId, long receiverRegId, FirestoreChildCallBack callBack);

    void addNewChatMessage(String chatId, long chatRoomId, Map<String, Object> messageMap);

    void getChatHistoryByChatRoomIdFragmentList(long senderRegId, FirestoreChildCallBack callBack);
}
