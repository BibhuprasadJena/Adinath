package com.release.eztoll.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.release.eztoll.Database.DBHelper;
import com.release.eztoll.Netwok.NetworkStatus;
import com.release.eztoll.R;
import com.release.eztoll.Retrofit.ConnectivityInterface;
import com.release.eztoll.Util.CommonUtility;
import com.release.eztoll.Util.PrefferenceManager;
import com.release.eztoll.Util.ProgressHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.release.eztoll.Util.ApiConstants.LOGIN;
import static com.release.eztoll.Util.ApiConstants.LOGIN_BASE_URL;
import static com.release.eztoll.Util.Constants.LOGIN_REQUEST;

public class LoginActivity extends AppCompatActivity implements ConnectivityInterface.ApiInterafce,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final int REQUEST_LOCATION_PERMISSION = 101;
    EditText Email,Password;
    CheckBox RememberMe;
    Button Login;
    TextView forgot_password;

    ProgressHandler progressHandler;
    PrefferenceManager prefferenceManager;
    private Toolbar mToolbar;
    String Email_Info,Password_Info;

    LinearLayout child_layout,main_layout;
    public static final int REQUEST_LOCATION = 001;
    GoogleApiClient googleApiClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    PendingResult<LocationSettingsResult> pendingResult;


    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(){
        Email = (EditText) findViewById(R.id.email);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        Password = (EditText) findViewById(R.id.password);
        RememberMe = (CheckBox) findViewById(R.id.remember_me);
        Login = (Button) findViewById(R.id.login);
        child_layout = (LinearLayout) findViewById(R.id.child_layout);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);


        if(CommonUtility.isTablet(LoginActivity.this)){
            main_layout.setBackgroundResource(R.drawable.tab_login_bg);
        }else {
            main_layout.setBackgroundResource(R.drawable.phone_login_bg);
        }




        prefferenceManager = PrefferenceManager.getFeaturePreference(LoginActivity.this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(4000);

        if (ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        final ImageView splash = (ImageView) findViewById(R.id.logo);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mEnableGps();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Follwing code is applicable for hide status bar of this activity.
         */
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login);
        _init();


        /**
         * Navigating form login page to Forgot password Activity .
         */
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgotPassword.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });



        /**
         * This method is used to check whether user has remember me permission or not .
         */
        if(userHasRememberMePermission()){
            Intent intent = new Intent(LoginActivity.this,UserDashBoard.class);
            startActivity(intent);
            finish();
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtility.hideKeyBoard(LoginActivity.this);
//                validateUser();

                if("eztoll".equals(Email.getText().toString().trim()) && Password.getText().toString().trim().equals("111111")){
                    progressHandler = new ProgressHandler(LoginActivity.this);
                    progressHandler.show();
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            timer.cancel();

                            try{
                                progressHandler.hide();
                                prefferenceManager.setDataInPref(PrefferenceManager.REMEMBER_ME,"1");
                                Intent intent = new Intent(LoginActivity.this,UserDashBoard.class);
                                startActivity(intent);
                                finish();
                            }catch (Exception e){}

                        }
                    },2000,100000);
                }else {
                    Toast.makeText(getApplicationContext(),"Please enter valid username or password.",Toast.LENGTH_SHORT).show();
                }




            }
        });

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

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    /**
     * This method is used to validate user credentials.
     */
    private void validateUser(){
        Email_Info = Email.getText().toString().trim();
        Password_Info = Password.getText().toString().trim();

        if(Email_Info.equals("") && Password_Info.equals("")){
            CommonUtility.showMessage(LoginActivity.this,"Please enter your email & password.");
            return;
        }

        if(Email_Info.equals("")){
            CommonUtility.showMessage(LoginActivity.this,"Please enter your email.");
            return;
        }

        if(Password_Info.equals("")){
            CommonUtility.showMessage(LoginActivity.this,"Please enter your password.");
            return;
        }

        if(!CommonUtility.isValidMail(Email_Info)){
            CommonUtility.showMessage(LoginActivity.this,"Please enter a valid email.");
            return;
        }


        if(!NetworkStatus.getInstance().isConnected(LoginActivity.this)){
            CommonUtility.showMessage(LoginActivity.this,"Please check your internet connectivity.");
            return;
        }


        /**
         * Call method to authenticate the user.
         */
        authenticateUser(Email_Info,Password_Info);

    }


    /**
     * This method is used to connect to server and authenticate the user .
     * @param Email
     * @param Password
     */
    private void authenticateUser(String Email , String Password){

        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("email",Email);
        inputHashMap.put("password",Password);
        inputHashMap.put("isAndroid","1");
        inputHashMap.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        inputHashMap.put("google_id",prefferenceManager.getDataFromPref(PrefferenceManager.GOOGLE_FCM_TOKEN,""));

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(LOGIN,inputHashMap,LOGIN_REQUEST,this,LOGIN_BASE_URL);
        connectivityInterface.startApiProcessing();
    }


    /**
     * This method will handle preExecute processing.
     */
    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(LoginActivity.this);
        progressHandler.show();
    }


    /**
     * This method will handle postExecute processing.
     */
    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
        }

        if(requestData == LOGIN_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if(response.contains("SUCCESS")){
                        prefferenceManager.setDataInPref(PrefferenceManager.REMEMBER_ME,"1");

                        String token = jsonObject.optString("token");
                        prefferenceManager.setDataInPref(PrefferenceManager.LOGIN_TOKEN,token);


                        JSONObject userData = jsonObject.optJSONObject("data");
                        String fname = userData.optString("fname").trim();
                        String mname = userData.optString("mname").trim();
                        String lname = userData.optString("lname").trim();
                        String prof_img = userData.optString("prof_image").trim();
                        String empid = userData.optString("empid").trim();
                        String Id = userData.optString("id").trim();

                        prefferenceManager.setDataInPref(PrefferenceManager.USER_ID,Id);


                        String fullName;
                        if(mname!=null && !mname.equals(""))
                            fullName= fname+" "+mname+" "+lname;
                        else
                            fullName= fname+" "+lname;

                        SQLiteDatabase DB = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                        Cursor cursor = DB.rawQuery("SELECT * FROM " + DBHelper.USER_TABLE + " WHERE Email = '" + Email_Info + "'", null);
                        int count = cursor.getCount();
                        if(count>0){
                            String Qry = "UPDATE " + DBHelper.USER_TABLE + " SET Name = '"+fullName+"' , Password = '"+Password_Info+"' , " +
                                    "Token = '"+token+"' , ProfileImage = '"+prof_img+"',EmpId='"+empid+"',Id='"+Id+"' WHERE Email = '"+Email_Info+"'";
                            DB.execSQL(Qry);
                        }else{
                            String Qry = "INSERT INTO " + DBHelper.USER_TABLE + "(Name,Password,Email,Token,ProfileImage,EmpId,Id) VALUES" +
                                    "('" + fullName + "','" + Password_Info + "','" + Email_Info + "'," +
                                    "'" + token + "','" + prof_img + "','" + empid + "','" + Id + "')";

                            DB.execSQL(Qry);
                        }

                        Intent intent = new Intent(LoginActivity.this,UserDashBoard.class);
                        startActivity(intent);
                        finish();



                    }else{
                        prefferenceManager.setDataInPref(PrefferenceManager.REMEMBER_ME,"0");
                        prefferenceManager.setDataInPref(PrefferenceManager.LOGIN_TOKEN,"");
                        CommonUtility.showMessage(LoginActivity.this,"Alert",message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    prefferenceManager.setDataInPref(PrefferenceManager.REMEMBER_ME,"0");
                    prefferenceManager.setDataInPref(PrefferenceManager.LOGIN_TOKEN,"");
                    CommonUtility.showMessage(LoginActivity.this,"Alert","Something went wrong,please try again.");
                }
            }else {
                prefferenceManager.setDataInPref(PrefferenceManager.REMEMBER_ME,"0");
                prefferenceManager.setDataInPref(PrefferenceManager.LOGIN_TOKEN,"");
                CommonUtility.showMessage(LoginActivity.this,"Alert","Something went wrong,please try again.");
            }

        }

    }


    /**
     * Following codes are using for GPS enabling.
     */

    private void mEnableGps() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        mLocationSetting();
    }

    public void mLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);

        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        mResult();

    }

    public void mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();


                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(LoginActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.


                        break;
                }
            }

        });
    }

    //callback method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:

                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                    default:
                        break;
                }
                break;

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }


}
