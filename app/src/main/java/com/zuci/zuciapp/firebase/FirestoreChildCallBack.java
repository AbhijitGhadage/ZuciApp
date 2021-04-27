package com.zuci.zuciapp.firebase;

public abstract class FirestoreChildCallBack {
    public abstract void onChildAdded(Object object);

    public abstract void onChildModified(Object object);

    public abstract void onChildRemoved(Object object);

    public abstract void onError(Object object);
}
