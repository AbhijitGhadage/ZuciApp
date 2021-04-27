package com.zuci.zuciapp.firebase.notification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.zuci.zuciapp.R;
import com.zuci.zuciapp.ui.agoraVideoCall.VideoCalleeActivity;
import com.zuci.zuciapp.ui.agoraVoiceCall.VoiceCalleeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject json) {

        try {
            String callType=json.getString("callType");
            if(callType.equalsIgnoreCase("AC")){
//                Intent resultIntent = new Intent(getApplicationContext(), VoiceCalleeActivity.class);
//                startActivity(resultIntent);

                Intent notificationIntent = new Intent(getApplicationContext() , VoiceCalleeActivity. class ) ;
                notificationIntent.putExtra( "NotificationMessage" , "I am from Notification" ) ;
                notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
                notificationIntent.setAction(Intent. ACTION_MAIN ) ;
                notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
                PendingIntent resultIntent = PendingIntent. getActivity (getApplicationContext() , 0 , notificationIntent , 0 ) ;
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext() ,
                        default_notification_channel_id )
                        .setSmallIcon(R.drawable. ic_launcher_foreground )
                        .setContentTitle( "Test" )
                        .setContentText( "Hello! This is my first push notification" )
                        .setContentIntent(resultIntent) ;
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;
                if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
                    int importance = NotificationManager. IMPORTANCE_HIGH ;
                    NotificationChannel notificationChannel = new
                            NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
                    mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
                    assert mNotificationManager != null;
                    mNotificationManager.createNotificationChannel(notificationChannel) ;
                }
                assert mNotificationManager != null;
                mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                        mBuilder.build()) ;

            }else if(callType.equalsIgnoreCase("VC")){
                Intent resultIntent = new Intent(getApplicationContext(), VideoCalleeActivity.class);
                startActivity(resultIntent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

/*
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
//            handleNotification(remoteMessage.getNotification().getBody());
        }*/
