package com.zuci.zuciapp.ui.firestoreMessageChat;

import com.zuci.zuciapp.firebase.CallBack;
import com.zuci.zuciapp.firebase.FirebaseConstant;
import com.zuci.zuciapp.firebase.FirebaseTemplateRepository;
import com.zuci.zuciapp.firebase.FirestoreChildCallBack;
import com.zuci.zuciapp.ui.firestoreMessageChat.messageChatHistory.ChatModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

public class MessageChatRepositoryImpl extends FirebaseTemplateRepository implements MessageChatRepository {
    @Override
    public long getChatRoomId(long senderRegId, long receiverRegId) {
        long tempChatRoomIdOne = String.valueOf(senderRegId).hashCode();
        long tempChatRoomIdTwo = String.valueOf(receiverRegId).hashCode();
        if (tempChatRoomIdOne < tempChatRoomIdTwo)
            return String.valueOf(senderRegId + receiverRegId).hashCode();
        else
            return String.valueOf(receiverRegId + senderRegId).hashCode();
    }

    @Override
    public void getChatHistoryByChatRoomId(long senderRegId, long receiverRegId, FirestoreChildCallBack callBack) {
        long chatRoomId = getChatRoomId(senderRegId, receiverRegId);
        Query query = FirebaseConstant.CHAT_HISTORY_COLLECTION_REF.document(String.valueOf(chatRoomId)).collection("message").orderBy("createdDateTime", Query.Direction.ASCENDING);
        fireStoreChildEventListener(query, new FirestoreChildCallBack() {
            @Override
            public void onChildAdded(Object object) {
                if (object != null) {
                    QueryDocumentSnapshot queryDocumentSnapshot = (QueryDocumentSnapshot) object;
                    ChatModel chatModel = queryDocumentSnapshot.toObject(ChatModel.class);
                    callBack.onChildAdded(chatModel);
                }
            }

            @Override
            public void onChildModified(Object object) {
            }

            @Override
            public void onChildRemoved(Object object) {
            }

            @Override
            public void onError(Object object) {
            }
        });
    }

    @Override
    public void addNewChatMessage(String chatId, long chatRoomId, Map<String, Object> messageMap) {
        DocumentReference documentReference = FirebaseConstant.CHAT_HISTORY_COLLECTION_REF.document(String.valueOf(chatRoomId)).collection("message").document(chatId);
        fireStoreCreateAndMerge(documentReference, messageMap, new CallBack() {
            @Override
            public void onSuccess(Object object) {
            }

            @Override
            public void onError(Object object) {
            }
        });
    }



    @Override
    public void getChatHistoryByChatRoomIdFragmentList(long senderRegId, FirestoreChildCallBack callBack) {
//        long chatRoomId = getChatRoomId(senderRegId, receiverRegId);
        Query query = FirebaseConstant.CHAT_HISTORY_COLLECTION_REF.document(String.valueOf(senderRegId)).collection("message").orderBy("createdDateTime", Query.Direction.DESCENDING);
        fireStoreChildEventListener(query, new FirestoreChildCallBack() {
            @Override
            public void onChildAdded(Object object) {
                if (object != null) {
                    QueryDocumentSnapshot queryDocumentSnapshot = (QueryDocumentSnapshot) object;
                    ChatModel chatModel = queryDocumentSnapshot.toObject(ChatModel.class);
                    callBack.onChildAdded(chatModel);
                }
            }

            @Override
            public void onChildModified(Object object) {
            }

            @Override
            public void onChildRemoved(Object object) {
            }

            @Override
            public void onError(Object object) {
            }
        });
    }

}
