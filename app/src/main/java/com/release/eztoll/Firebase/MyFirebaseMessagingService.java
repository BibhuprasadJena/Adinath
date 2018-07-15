package com.release.eztoll.Firebase;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.release.eztoll.Activity.UserDashBoard;
import com.release.eztoll.R;
import com.release.eztoll.Util.PrefferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.prefs.PreferenceChangeEvent;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "ADINATH1";
    String MESSAGE = "";
    PrefferenceManager prefferenceManager;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        prefferenceManager = PrefferenceManager.getFeaturePreference(MyFirebaseMessagingService.this);


        Intent intent = new Intent("Complain_Recievre");
        sendBroadcast(intent);

//        showNotification("fhqegf","sfvqyfgqwuidgqwuidgqw");

        if (remoteMessage == null)
            return;


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

    }


    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        try {
            JSONObject data = json.getJSONObject("data");

            String user_id = data.optString("user_id");
            String message = data.optString("message");
            String message_title = data.optString("message_title");
            MESSAGE = message;

            Log.e(TAG, "user_id: =======" + user_id);
            Log.e(TAG, "message: ==========" + message);
            Log.e(TAG, "message_title: ==========" + message_title);
            Log.e(TAG, "user_id: ==========" + user_id.equals(prefferenceManager.getDataFromPref(PrefferenceManager.USER_ID, PrefferenceManager.DEFAULT_USER_ID)));

            if (userHasRememberMePermission() && (user_id.equals(prefferenceManager.getDataFromPref(PrefferenceManager.USER_ID, PrefferenceManager.DEFAULT_USER_ID)))) {
                // Show Notification
                showNotification(message, message_title);
            }


        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * This method is used to check whether user has remember me permission or not .
     */
    private boolean userHasRememberMePermission() {

        if (prefferenceManager.getDataFromPref(PrefferenceManager.REMEMBER_ME, PrefferenceManager.DEFAULT_REMEMBER_ME).equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    private void showNotification(String message, String message_title) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo) // notification icon
                .setContentTitle(message) // title for notification
                .setContentText(message_title) // message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(this, UserDashBoard.class);
        @SuppressLint("WrongConstant") PendingIntent pi = PendingIntent.getActivity(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

}