package com.release.eztoll.Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by MUVI on 27-Jan-18.
 */

public interface RetrofitApiInterface{

    @FormUrlEncoded
    @POST("{apiName}")
    Call<ResponseBody> dynamicApi(@Path("apiName") String apiname, @FieldMap Map<String, String> parameters);
    @FormUrlEncoded
    @POST("{apiName}")
    Call<ResponseBody> dynamicApi(@Path("apiName") String apiname, @Field("token") String token, @FieldMap Map<String, JSONObject> parameters1, @Field("isAndroid") String isAndroid );

}