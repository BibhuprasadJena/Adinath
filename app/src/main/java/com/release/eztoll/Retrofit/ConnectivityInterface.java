package com.release.eztoll.Retrofit;

import com.release.eztoll.Activity.RetrofitApiClient;
import com.release.eztoll.Activity.RetrofitApiInterface;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MUVI on 28-Jan-18.
 */

public class ConnectivityInterface {

    public interface ApiInterafce {

        public void onTaskPreExecute();
        public void onTaskPostExecute(String response, int requestData);

    }

    public static String BASE_URL = "http://stage.alysiantech.com/agent_app/users/";
    static String apiName = "";
    static int requestData = 0;
    static String customBaseUrl = "";
    static HashMap<String,String> apiParameter;
    static ApiInterafce listener;
    HashMap<String,JSONObject> stringHashMapHashMap;
    boolean callCustomApi = false;
    String token;




    public ConnectivityInterface(String apiName , HashMap<String , String > apiParameter, int requestData, ApiInterafce listener , String customBaseUrl){
        this.apiName = apiName;
        this.apiParameter = apiParameter;
        this.requestData = requestData;
        this.listener = listener;
        this.customBaseUrl = customBaseUrl;
        BASE_URL = customBaseUrl;
        callCustomApi = false;
    }

    public ConnectivityInterface(String apiName , int requestData, ApiInterafce listener , String customBaseUrl , HashMap<String ,JSONObject>  stringHashMapHashMap ,String token){
        this.apiName = apiName;
        this.apiParameter = apiParameter;
        this.requestData = requestData;
        this.listener = listener;
        this.customBaseUrl = customBaseUrl;
        BASE_URL = customBaseUrl;
        this.stringHashMapHashMap = stringHashMapHashMap;
        callCustomApi = true;
        this.token = token;
    }


    public void startApiProcessing(){

        listener.onTaskPreExecute();

        RetrofitApiInterface retrofitApiInterface = RetrofitApiClient.getClient(BASE_URL).create(RetrofitApiInterface.class);
        Call<ResponseBody> dyResponseBodyCall = null;

        if(callCustomApi){

            dyResponseBodyCall = retrofitApiInterface.dynamicApi(apiName,token,stringHashMapHashMap,"1");
        }else{
            dyResponseBodyCall = retrofitApiInterface.dynamicApi(apiName,apiParameter);
        }

        dyResponseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responsedata  = response.body().string();
                        listener.onTaskPostExecute(responsedata,requestData);

                } catch (Exception e) {
                    listener.onTaskPostExecute("exp="+e.toString(),requestData);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onTaskPostExecute("failed="+t.getMessage(),requestData);
            }
        });
    }
}

