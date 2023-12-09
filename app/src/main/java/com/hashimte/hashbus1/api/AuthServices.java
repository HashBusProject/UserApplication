package com.hashimte.hashbus1.api;

import com.hashimte.hashbus1.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AuthServices {
    @GET("/login")
    Call<User> login();
}
