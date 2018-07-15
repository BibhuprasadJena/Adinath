package com.release.eztoll.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.release.eztoll.Database.DBHelper;
import com.release.eztoll.Netwok.NetworkStatus;
import com.release.eztoll.R;
import com.release.eztoll.Retrofit.ConnectivityInterface;
import com.release.eztoll.Util.CommonUtility;
import com.release.eztoll.Util.PrefferenceManager;
import com.release.eztoll.Util.ProgressHandler;

import org.json.JSONObject;

import java.util.HashMap;

import static com.release.eztoll.Util.ApiConstants.FORGOT_PASSWORD;
import static com.release.eztoll.Util.ApiConstants.FORGOT_PASSWORD_BASE_URL;
import static com.release.eztoll.Util.ApiConstants.LOGIN;
import static com.release.eztoll.Util.Constants.FORGOT_PASSWORD_REQUEST;
import static com.release.eztoll.Util.Constants.LOGIN_REQUEST;
import static com.release.eztoll.Util.Constants.LOGOUT_REQUEST;

public class ForgotPassword extends AppCompatActivity implements ConnectivityInterface.ApiInterafce{

    Toolbar mToolbar;
    EditText Email;
    Button submit;
    LinearLayout main_layout;
    PrefferenceManager prefferenceManager;
    ProgressHandler progressHandler;
    String Email_Info="";


    /**
     * This method is only responsible to initialize the view components.
     */
    private void _init(){
        Email = (EditText) findViewById(R.id.email);
        submit = (Button) findViewById(R.id.submit);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);


        if(CommonUtility.isTablet(ForgotPassword.this)){
            main_layout.setBackgroundResource(R.drawable.tab_login_bg);
        }else {
            main_layout.setBackgroundResource(R.drawable.phone_login_bg);
        }


        prefferenceManager = PrefferenceManager.getFeaturePreference(ForgotPassword.this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle("Forgot Paassword");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        _init();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validateUser();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }



    /**
     * This method is used to valiadate user credentials.
     */
    private void validateUser(){
        Email_Info = Email.getText().toString().trim();

        if(Email_Info.equals("")){
            CommonUtility.showMessage(ForgotPassword.this,"Please enter your email.");
            return;
        }

        if(!CommonUtility.isValidMail(Email_Info)){
            CommonUtility.showMessage(ForgotPassword.this,"Please enter a valid email.");
            return;
        }

        if(!NetworkStatus.getInstance().isConnected(ForgotPassword.this)){
            CommonUtility.showMessage(ForgotPassword.this,"Please check your internet connectivity.");
            return;
        }


        /**
         * Call method to authenticate the user.
         */
        authenticateUser(Email_Info);

    }

    /**
     * This method is used to connect to server and authenticate the user .
     * @param Email
     */
    private void authenticateUser(String Email){
        HashMap<String ,String> inputHashMap = new HashMap<>();
        inputHashMap.put("email",Email);

        ConnectivityInterface connectivityInterface = new ConnectivityInterface(FORGOT_PASSWORD,inputHashMap,FORGOT_PASSWORD_REQUEST,this,FORGOT_PASSWORD_BASE_URL);
        connectivityInterface.startApiProcessing();
    }

    @Override
    public void onTaskPreExecute() {
        progressHandler = new ProgressHandler(ForgotPassword.this);
        progressHandler.show();
    }

    @Override
    public void onTaskPostExecute(String response, int requestData) {
        if(progressHandler!=null && progressHandler.isShowing()){
            progressHandler.hide();
        }

        if(requestData == FORGOT_PASSWORD_REQUEST){

            if(response!=null && response.contains("status")){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.optString("message");


                    if(response.contains("SUCCESS")){
                        CommonUtility.showMessage(ForgotPassword.this,message,true);
                    }else{
                        CommonUtility.showMessage(ForgotPassword.this,"Alert",message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtility.showMessage(ForgotPassword.this,"Alert","Something went wrong,please try again.");
                }
            }else {
                CommonUtility.showMessage(ForgotPassword.this,"Alert","Something went wrong,please try again.");
            }

        }
    }


}
