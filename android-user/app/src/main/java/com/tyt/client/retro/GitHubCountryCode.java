package com.tyt.client.retro;


import com.tyt.client.retro.responsemodel.CountryCodeModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Mahi in 2021.
 */

public interface GitHubCountryCode {
    @GET("/json")
    Call<CountryCodeModel> getCurrentCountry();
}
