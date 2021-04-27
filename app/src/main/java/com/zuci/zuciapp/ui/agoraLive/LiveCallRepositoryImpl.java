package com.zuci.zuciapp.ui.agoraLive;

import android.util.Log;

import com.zuci.zuciapp.firebase.CallBack;
import com.zuci.zuciapp.firebase.FirebaseConstant;
import com.zuci.zuciapp.firebase.FirebaseTemplateRepository;
import com.zuci.zuciapp.ui.agoraLive.liveVideo.LiveResponse;
import com.zuci.zuciapp.ui.agoraVoiceCall.CallResponse;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LiveCallRepositoryImpl extends FirebaseTemplateRepository implements LiveCallRepository {
    @Override
    public void addOrUpdateLiveCallModel(DocumentReference documentReference, Object callResponse) {
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
    public void notifyLiveCallStatusChanged(long registrationId, CallBack callBack) {
        Query query = FirebaseConstant.LIVE_CALL_HISTORY_COLLECTION_REF.whereEqualTo("firebaseCallId", registrationId).limit(1);
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
    public void getLiveCallList(long regId, CallBack callBack) {
        Query query = FirebaseConstant.LIVE_CALL_HISTORY_COLLECTION_REF.whereEqualTo("liveStatus", true).orderBy("liveStartDateTime");
        firestoreReadDataListener(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                List<LiveResponse> liveResponseList = new ArrayList<>();
                QuerySnapshot queryDocumentSnapshots = (QuerySnapshot) object;
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        LiveResponse liveResponse = documentSnapshot.toObject(LiveResponse.class);
                        if (liveResponse.getFollowingList() != null)
                            for (FollowingListModel followingListModel : liveResponse.getFollowingList())
                                if (followingListModel.getFollowingRegistrationID() == regId) {
                                    LiveResponse liveUserModel = new LiveResponse();
                                    liveUserModel.setRegId(liveResponse.getRegId());
                                    liveUserModel.setUserName(liveResponse.getUserName());
                                    liveUserModel.setUserProfileImage(liveResponse.getUserProfileImage());
                                    liveUserModel.setChannelName(liveResponse.getChannelName());
                                    liveUserModel.setUserLiveCallCoins(liveResponse.getUserLiveCallCoins());
                                    liveResponseList.add(liveUserModel);
                                    break;
                                }
                    }
                }
                callBack.onSuccess(liveResponseList);
            }

            @Override
            public void onError(Object object) {

            }
        });

       /* Query query = FirebaseConstant.LIVE_CALL_HISTORY_COLLECTION_REF.whereArrayContains("callRoomList", regId);
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
        });*/
    }


}
