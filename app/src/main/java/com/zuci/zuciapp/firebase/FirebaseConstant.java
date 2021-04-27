package com.zuci.zuciapp.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseConstant {
    public static final DatabaseReference DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference();
    public static final DatabaseReference CALL_HISTORY_REFERENCE = DATABASE_REFERENCE.child("callHistory");

    //firestore
    public static final FirebaseFirestore FIRESTORE_DATABASE = FirebaseFirestore.getInstance();
    public static final CollectionReference CALL_HISTORY_COLLECTION_REF = FIRESTORE_DATABASE.collection("callHistory");
    public static final CollectionReference CHAT_HISTORY_COLLECTION_REF = FIRESTORE_DATABASE.collection("chatHistory");
    public static final CollectionReference CALL_HISTORY_SUB_COLLECTION_REF = FIRESTORE_DATABASE.collection("message");
    public static final CollectionReference LIVE_CALL_HISTORY_COLLECTION_REF = FIRESTORE_DATABASE.collection("liveCallHistory");
}
