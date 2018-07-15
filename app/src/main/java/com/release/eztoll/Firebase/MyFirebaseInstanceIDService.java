package com.release.eztoll.Firebase;

import android.content.Intent;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.release.eztoll.Util.PrefferenceManager;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    PrefferenceManager prefferenceManager;

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        prefferenceManager = new PrefferenceManager(this);
        prefferenceManager.setDataInPref(PrefferenceManager.GOOGLE_FCM_TOKEN,refreshedToken);

        // Saving reg id to shared preferences
        Log.v("BIBHU11", "sendRegistrationToServer: " + refreshedToken);

        onCreate();
    }

}
