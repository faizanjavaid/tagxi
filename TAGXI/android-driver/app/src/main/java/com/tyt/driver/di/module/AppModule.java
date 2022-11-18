package com.tyt.driver.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.tyt.driver.app.MyApp;
import com.tyt.driver.utilz.SharedPrefence;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tyt.driver.retro.GitHubCountryCode;
import com.tyt.driver.retro.GitHubMapService;
import com.tyt.driver.retro.GitHubService;
import com.tyt.driver.retro.dynamicInterceptor;
import com.tyt.driver.utilz.Constants;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 9/22/17.
 */


/*subcomponents = {
 *//* Add additional sub components here *//*}*/
@Module
public class AppModule {
    private SharedPrefence sharedPrefence;

    private Context context;
    public AppModule(@NonNull Application application){
        this.context = application;
    }

    public AppModule(){

    }

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    dynamicInterceptor provideInterceptor() { // This is where the Interceptor object is constructed
        return new dynamicInterceptor();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    SharedPreferences.Editor providesSharedPreferencesEditor(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit();
    }


    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    @Named(Constants.ourApp)
    OkHttpClient.Builder provideOkHttpClientInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        return httpClient;
    }

    @Provides
    @Singleton
    @Named(Constants.googleMap)
    OkHttpClient.Builder provideOkHttpCterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


        httpClient.addInterceptor(logging);


        return httpClient;
    }


    @Provides
    @Singleton
    @Named(Constants.ourApp)
    GitHubService provideRetrofit(@Named(Constants.ourApp) Retrofit.Builder builder) {

        return builder.build().create(GitHubService.class);
    }


    @Provides
    @Singleton
    @Named(Constants.googleMap)
    GitHubMapService provideRetrofitgoogle(@Named(Constants.googleMap) Retrofit.Builder builder) {

        return builder.build().create(GitHubMapService.class);
    }

    @Provides
    @Named(Constants.ourApp)
    @Singleton
    Retrofit.Builder provideRetrofitBuilder(Gson gson, @Named(Constants.ourApp) OkHttpClient.Builder okhttpbuilder) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okhttpbuilder.build())
                .baseUrl(Constants.URL.BaseURL);
    }


    @Provides
    @Named(Constants.googleMap)
    @Singleton
    Retrofit.Builder provideRetrzzofit(Gson gson, @Named(Constants.googleMap) OkHttpClient.Builder okhttpbuilder) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okhttpbuilder.build())
                .baseUrl(Constants.URL.GooglBaseURL);
    }

    @Provides
    @Singleton
    @Named(Constants.countryMap)
    GitHubCountryCode provideRetrofitCountryCode(@Named(Constants.countryMap) Retrofit.Builder builder) {

        return builder.build().create(GitHubCountryCode.class);
    }


    @Provides
    @Named(Constants.countryMap)
    @Singleton
    Retrofit.Builder provideRetroBuilderCountryCode(Gson gson, @Named(Constants.googleMap) OkHttpClient.Builder okhttpbuilder) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okhttpbuilder.build())
                .baseUrl(Constants.URL.COUNTRY_CODE_URL);
    }

    @Provides
    @Singleton
    HashMap<String, String> provideHashMap() {

        return new HashMap<String, String>();
    }


    @Provides
    @Singleton
    Socket provideSocket() {
        Socket socket = null;

        try {
            socket = IO.socket("");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return socket;
    }


}