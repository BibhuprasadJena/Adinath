package com.release.eztoll.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.release.eztoll.Database.DBHelper;
import com.release.eztoll.R;
import com.release.eztoll.Util.CommonUtility;
import com.release.eztoll.Util.PrefferenceManager;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.internal.Util;

public class SplashScreen extends AppCompatActivity {
    PrefferenceManager prefferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // return 0.75 if it's LDPI
        // return 1.0 if it's MDPI
        // return 1.5 if it's HDPI
        // return 2.0 if it's XHDPI
        // return 3.0 if it's XXHDPI
        // return 4.0 if it's XXXHDPI
        float density = getResources().getDisplayMetrics().density;
        Log.v("ADINATH","density = "+density);

        /**
         * Follwing code is applicable for hide status bar of this activity.
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.splash_screen);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        prefferenceManager = new PrefferenceManager(SplashScreen.this);


        /**
         * Database created.
         * getWritable permission will allow to create table inside the database .
         */

        DBHelper dbHelper = new DBHelper(SplashScreen.this);
        dbHelper.getReadableDatabase();
        dbHelper.getWritableDatabase();



        /**
         * Following timer will hold the screen for few seconds amd then allow to navigate to other screen .
         */
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

               // if(!(prefferenceManager.getDataFromPref(PrefferenceManager.GOOGLE_FCM_TOKEN,"")).equals("")){
                    timer.cancel();
                    Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
//                }

            }
        },2000,20000);
    }
}
