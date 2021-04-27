package com.zuci.zuciapp.firebase;

import android.util.Log;

import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CallHistoryRepositoryImpl extends FirebaseTemplateRepository implements CallHistoryRepository {

    @Override
    public void addOrUpdateCallModel(DocumentReference documentReference, Object callResponse) {
        fireStoreCreateAndMerge(documentReference, callResponse, new CallBack() {
            @Override
            public void onSuccess(Object object) {
            }

            @Override
            public void onError(Object object) {
            }
        });
    }

    @Override
    public void notifyCallReceived(long regId, FirestoreChildCallBack callBack) {
        Query query = FirebaseConstant.CALL_HISTORY_COLLECTION_REF.whereEqualTo("ReceiverUserId", regId).whereEqualTo("CallStatus", "Active");
        fireStoreChildEventListener(query, new FirestoreChildCallBack() {
            @Override
            public void onChildAdded(Object object) {
                if (object != null) {
                    QueryDocumentSnapshot queryDocumentSnapshot = (QueryDocumentSnapshot) object;
                    CallResponse callResponse = queryDocumentSnapshot.toObject(CallResponse.class);
                    callBack.onChildAdded(callResponse);
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
    public void notifyVoiceCallStatusChanged(String firebaseCallId, CallBack callBack) {
        Query query = FirebaseConstant.CALL_HISTORY_COLLECTION_REF.whereEqualTo("firebaseCallId", firebaseCallId).whereEqualTo("CallType", "AC").limit(1);
        firestoreReadDataListener(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    try {
                        QuerySnapshot querySnapshot = (QuerySnapshot) object;
//                    if (querySnapshot.size() > 0) { // abhijit add condition
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        if (documentSnapshot != null)
                            callBack.onSuccess(documentSnapshot.toObject(CallResponse.class));
                    } catch (Exception e) {
                        Log.e("", e.getMessage());
                    }
//                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void notifyVideoCallStatusChanged(String firebaseCallId, CallBack callBack) {
        Query query = FirebaseConstant.CALL_HISTORY_COLLECTION_REF.whereEqualTo("firebaseCallId", firebaseCallId).whereEqualTo("CallType", "VC").limit(1);
        firestoreReadDataListener(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    try {
                        QuerySnapshot querySnapshot = (QuerySnapshot) object;
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        if (documentSnapshot != null)
                            callBack.onSuccess(documentSnapshot.toObject(CallResponse.class));
                    } catch (Exception e) {
                        Log.e("", e.getMessage());
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void getCallHistory(long regId, CallBack callBack) {
        Query query = FirebaseConstant.CALL_HISTORY_COLLECTION_REF.whereArrayContains("callRoomList", regId).orderBy("callDateTime");
        firestoreRead(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                ArrayList<CallResponse> callResponseArrayList = new ArrayList<>();
                QuerySnapshot queryDocumentSnapshots = (QuerySnapshot) object;
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        CallResponse callResponse = documentSnapshot.toObject(CallResponse.class);
                        callResponseArrayList.add(callResponse);
                    }
                    callBack.onSuccess(callResponseArrayList);
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }
}
