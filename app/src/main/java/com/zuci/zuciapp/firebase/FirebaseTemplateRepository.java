package com.zuci.zuciapp.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zuci.zuciapp.ui.agoraVoiceCall.ConstantApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

public abstract class FirebaseTemplateRepository {
    protected final void firebaseCreateCallHistory(final DatabaseReference databaseReference, final Map<String, Object> modelMap, CallBack callBack) {
        try {
            databaseReference.keepSynced(true);
            databaseReference.updateChildren(modelMap, (error, ref) -> {
                if (error == null) {
                    callBack.onSuccess(null);
                } else {
                    callBack.onError(error);
                }
            });
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
    }

    protected final void updateFirebaseData(final DatabaseReference databaseReference, final Map<String, Object> modelMap, CallBack callBack) {
        databaseReference.keepSynced(true);
        databaseReference.updateChildren(modelMap, (error, ref) -> {
            if (error == null) {
                callBack.onSuccess(null);
            } else {
                callBack.onError(error);
            }
        });
    }

    protected final void addValueEventListenerFirebase(final Query query, CallBack callBack) {
        query.keepSynced(true);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                callBack.onSuccess(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    protected final void fireStoreUpdate(final DocumentReference documentReference, final Map<String, Object> object, final CallBack callback) {
        documentReference.update(object)
                .addOnSuccessListener(aVoid -> callback.onSuccess(ConstantApp.SUCCESS))
                .addOnFailureListener(e -> callback.onError(e));
    }

    protected final void fireStoreCreateAndMerge(final DocumentReference documentReference, final Object object, final CallBack callback) {
        documentReference.set(object, SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onSuccess(ConstantApp.SUCCESS))
                .addOnFailureListener(e -> callback.onError(e));
    }

    protected final ListenerRegistration fireStoreChildEventListener(final com.google.firebase.firestore.Query query, final FirestoreChildCallBack firestoreChildCallBack) {
        return query.addSnapshotListener((value, e) -> {
            if (e != null) {
                firestoreChildCallBack.onError(e);
                return;
            }
            if (value != null) {
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            firestoreChildCallBack.onChildAdded(dc.getDocument());
                            break;
                        case MODIFIED:
                            firestoreChildCallBack.onChildModified(dc.getDocument());
                            break;
                        case REMOVED:
                            firestoreChildCallBack.onChildRemoved(dc.getDocument());
                            break;
                    }
                }
            }
        });
    }

    protected final ListenerRegistration firestoreReadDataListener(final com.google.firebase.firestore.Query query, final CallBack callBack) {
        return query.addSnapshotListener((value, e) -> {
            if (e != null) {
                callBack.onError(e);
                return;
            }
            callBack.onSuccess(value);
        });
    }

    protected final void firestoreRead(final com.google.firebase.firestore.Query query, final CallBack callBack) {
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    callBack.onSuccess(querySnapshot);
                } else {
                    callBack.onSuccess(null);
                }
            } else {
                callBack.onError(task.getException());
            }
        }).addOnFailureListener(e -> callBack.onError(e));
    }
}
