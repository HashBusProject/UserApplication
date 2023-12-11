package com.hashimte.hashbus1.api;

import com.google.gson.Gson;
import com.hashimte.hashbus1.model.User;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public class AuthServicesImp implements AuthServices{

    private final static String HTTPS = "http://192.168.1.28:8080";
    private final static String HTTPS1 = "https://global-memento-407716.uc.r.appspot.com/";

    private final static AuthServicesImp instance = new AuthServicesImp();

    private Retrofit retrofit = getRetrofit();

    private AuthServicesImp() {

    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(HTTPS1)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public static AuthServicesImp getInstance() {
        return instance;
    }

    @POST("/User/Login")
    public Call<User> login(@Body User user) {
        AuthServices authServices = retrofit.create(AuthServices.class);
        return authServices.login(user);
    }

    @POST("/User/SignUp")
    public Call<User> SignUp(@Body User user) {
        return retrofit.create(AuthServices.class).SignUp(user);
    }

    @PUT("/User/ChangePassword")
    public Call<User> changePassword(@Body User user) {
        return retrofit.create(AuthServices.class).changePassword(user);
    }
}
