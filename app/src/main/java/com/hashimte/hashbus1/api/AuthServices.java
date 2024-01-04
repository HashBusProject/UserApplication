package com.hashimte.hashbus1.api;

import com.hashimte.hashbus1.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthServices {
    @POST("/User/Login")
    Call<User> login(@Body User user);

    @POST("/User/SignUp")
    Call<User> SignUp(@Body User user);

    @PUT("/User/ForgetPassword")
    Call<User> changePassword(@Body User user);
}
