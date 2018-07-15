package com.release.eztoll.Retrofit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.release.eztoll.R;
import com.release.eztoll.Retrofit.ConnectivityInterface;

import java.util.HashMap;

public class RetrofitActivity extends AppCompatActivity implements ConnectivityInterface.ApiInterafce {

    Button send;
    TextView data;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        send = (Button) findViewById(R.id.all);
        data = (TextView) findViewById(R.id.all);


/*
        RetrofitApiInterface retrofitApiInterface1 = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<ResponseBody> myCall = retrofitApiInterface1.singleApi("isRegistrationEnabled","25e74a5c88d19c4b57c8138bf47abdf7");
        myCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.v("BIBHU1","response body= "+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });*/





        final HashMap parameters = new HashMap<>();
        parameters.put("authToken", "25e74a5c88d19c4b57c8138bf47abdf7");
        parameters.put("content_uniq_id", "b3acd62aaa10103ae0e9cea9f031ac54");
        parameters.put("stream_uniq_id", "cd222bdc2af51646483a4ae9271074b6");
        parameters.put("user_id", "151404");
        parameters.put("internet_speed", "0");
//      final ConnectivityInterface connectivityInterface = new ConnectivityInterface("GetVideoDetails",parameters,"Response of getVideoDetails",this);
//      final ConnectivityInterface connectivityInterface = new ConnectivityInterface("isRegistrationEnabled",parameters,"Response of isRegistrationEnabled",this);
      final ConnectivityInterface connectivityInterface = new ConnectivityInterface("",parameters,1,this,"https://api.ipify.org/");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.setText("");
                connectivityInterface.startApiProcessing();

            }
        });





   /*     call.enqueue(new Callback<RetrofitResponseModel>() {
            @Override
            public void onResponse(Call<RetrofitResponseModel>call, Response<RetrofitResponseModel> response) {
                Log.v("BIBHU1","response from retrofit code = "+response.body().getCode());
                Log.v("BIBHU1","response from retrofit status = "+response.body().getStatus());
                Log.v("BIBHU1","response body= "+response.raw().body().toString());
                Log.v("BIBHU1","response call= "+call.toString());
            }

            @Override
            public void onFailure(Call<RetrofitResponseModel>call, Throwable t) {
                // Log error here since request failed
            }
        });*/


    }

    @Override
    public void onTaskPreExecute() {
      progressDialog = ProgressDialog.show(RetrofitActivity.this,"","Please Wait ..");
    }

    @Override
    public void onTaskPostExecute(String response , int requestData) {
        progressDialog.cancel();
        data.setText(requestData+"\n\n"+response);
    }
/*
    public void startApiProcessing(final String apiName , HashMap<String,String> parameters){
        RetrofitApiInterface retrofitApiInterface = RetrofitApiClient.getClient(BASE_URL).create(RetrofitApiInterface.class);
        Call <ResponseBody> dyResponseBodyCall = retrofitApiInterface.dynamicApi(apiName,parameters);
        dyResponseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                   String responsedata  = response.body().string();


                   *//* Log.v("BIBHU123","response body= "+responsedata);
                    JSONObject jsonObject = new JSONObject(responsedata);
                    String msg = jsonObject.optString("msg");
                    String code = jsonObject.optString("code");
                    String status = jsonObject.optString("status");
                    data.setText("msg = "+msg+"\n"+"code = "+code+"\n"+"status = "+status);*//*


                    String prev_Value = data.getText().toString();
                    data.setText("Response of "+apiName+" : "+"\n"+prev_Value+"\n\n"+responsedata);
                } catch (Exception e) {
                    data.setText("Exception : "+"\n"+e.toString());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                data.setText("Throwable : "+"\n"+t.getMessage());
            }
        });
    }*/
}
