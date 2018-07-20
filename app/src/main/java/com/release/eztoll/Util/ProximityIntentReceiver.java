package com.release.eztoll.Util;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.release.eztoll.Database.DBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class ProximityIntentReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 1000;
    private int ID = 0;
    PrefferenceManager prefferenceManager;
    int previous_amt;

    //int i;
    @Override
    public void onReceive(final Context context, final Intent intent) {

//        Toast.makeText(context,"Proximity receiver called",Toast.LENGTH_LONG).show();

        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Bundle results = getResultExtras(true);
        Boolean entering = intent.getBooleanExtra(key, false);
        Log.v("EZTOLL1", "res= " + intent.getStringExtra("price"));
        Log.v("EZTOLL1", "res= " + intent.getStringExtra("toll_name"));
        Log.v("EZTOLL1", "res= " + intent.getStringExtra("vowner"));
        Log.v("EZTOLL1", "res= " + intent.getStringExtra("vno"));

        if (entering) {

            prefferenceManager = PrefferenceManager.getFeaturePreference(context);
            previous_amt = Integer.parseInt(prefferenceManager.getDataFromPref(PrefferenceManager.WALLET_AMOUNT, PrefferenceManager.DEFAULT_WALLET_AMOUNT));


            if (prefferenceManager.getDataFromPref(PrefferenceManager.CONFIG, PrefferenceManager.DEFAULT_CONFIG).trim().equals("1")) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setTitle("Toll Name : " + intent.getStringExtra("toll_name"));
                builder1.setMessage("Toll Amount : ₹ " + intent.getStringExtra("price")+"\n"+"Dou you want to pay for this toll ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                try {
                                    if (previous_amt < Integer.parseInt(intent.getStringExtra("price"))) {
                                        Toast.makeText(context, "You have insufficient wallet amount to pay the toll", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                } catch (Exception e) {
                                }

                                try {
                                    int current_balance = previous_amt - Integer.parseInt(intent.getStringExtra("price"));
                                    prefferenceManager.setDataInPref(PrefferenceManager.WALLET_AMOUNT, "" + current_balance);
                                    Toast.makeText(context, "Toll paymnet deducted.", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                }


                            }
                        });

                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else {



                try {
                    if (previous_amt < Integer.parseInt(intent.getStringExtra("price"))) {
                        Toast.makeText(context, "You have insufficient wallet amount to pay the toll", Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (Exception e) {
                }


                try {

                    int current_balance = previous_amt - Integer.parseInt(intent.getStringExtra("price"));
                    prefferenceManager.setDataInPref(PrefferenceManager.WALLET_AMOUNT, "" + current_balance);
                    Toast.makeText(context, "Toll paymnet deducted.", Toast.LENGTH_LONG).show();
                } catch (Exception e1) {
                }
            }




            Date date = Calendar.getInstance().getTime();

            // Display a date in day, month, year format
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            formatter = new SimpleDateFormat("dd MMM yyyy");
            String today = formatter.format(date);

            SQLiteDatabase DB = context.openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);

            String Qry = "INSERT INTO " + DBHelper.TRANSACTION_TABLE + "(Id,Amount,TranStatus,Date,TollName,Vno,Vowner) VALUES" +
                    "('" + 1 + "','" + intent.getStringExtra("price") + "','" + 0 + "','" + today + "','" + intent.getStringExtra("toll_name") + "'," +
                    "'" + intent.getStringExtra("vno") + "','" + intent.getStringExtra("vowner") + "')";

            DB.execSQL(Qry);

        } else {
            Log.v("EZTOLL1", "exiting ");

        }

    }
}
