package com.zuci.zuciapp.service;

import android.os.Build;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.PhoneAccountHandle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public abstract  class MyConnectionService extends ConnectionService {
    private static final String TAG = MyConnectionService.class.getSimpleName();



    @Override
    public Connection onCreateIncomingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {

        Toast.makeText(this, "onCreateIncomingConnection()", Toast.LENGTH_SHORT).show();

        return null;
    }

    @Override
    public Connection onCreateOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {

        Toast.makeText(this, "onCreateOutgoingConnection()", Toast.LENGTH_SHORT).show();
        return null;
    }


    @Override
    public void onCreateIncomingConnectionFailed(PhoneAccountHandle phoneAccount, ConnectionRequest request) {
        Log.e(TAG, "onCreateIncomingConnectionFailed: called. " + phoneAccount + " " + request);


        Toast.makeText(this, "onCreateIncomingConnectionFailed()", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCreateOutgoingConnectionFailed(PhoneAccountHandle phoneAccount, ConnectionRequest request) {
        Log.e(TAG, "onCreateOutgoingConnectionFailed: called. " + phoneAccount + " " + request);


        Toast.makeText(this, "onCreateOutgoingConnectionFailed()", Toast.LENGTH_SHORT).show();
    }
}
