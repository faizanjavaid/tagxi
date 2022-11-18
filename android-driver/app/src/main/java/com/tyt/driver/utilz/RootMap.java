package com.tyt.driver.utilz;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RootMap {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        Gson gson1 = new GsonBuilder()
                .setLenient()
                .create();
        Gson gson2 = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL.GooglBaseURL)
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson1))
                    .build();
        }
        return retrofit;
    }
}