package com.zuci.zuciapp.firebase;

import com.google.firebase.firestore.DocumentReference;

public interface CallHistoryRepository {
    void addOrUpdateCallModel(DocumentReference documentReference, Object callResponse);

    void notifyCallReceived(long regId, FirestoreChildCallBack callBack);

    void notifyVoiceCallStatusChanged(String firebaseCallId, CallBack callBack);

    void notifyVideoCallStatusChanged(String firebaseCallId, CallBack callBack);

    void getCallHistory(long regId, CallBack callBack);
}
