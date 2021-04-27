package com.zuci.zuciapp.ui.agoraLive;

import com.zuci.zuciapp.firebase.CallBack;
import com.google.firebase.firestore.DocumentReference;

public interface LiveCallRepository {
    void addOrUpdateLiveCallModel(DocumentReference documentReference, Object callResponse);

    void notifyLiveCallStatusChanged(long registrationId, CallBack callBack);

    void getLiveCallList(long regId, CallBack callBack);
}
