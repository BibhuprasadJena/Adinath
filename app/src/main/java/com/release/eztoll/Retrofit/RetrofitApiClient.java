package com.release.eztoll.Activity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Bibhu .
 */

public class RetrofitApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String BASE_URL) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        return retrofit;
    }
}


