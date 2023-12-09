package com.hashimte.hashbus1.api;

import android.util.Log;

import com.hashimte.hashbus1.model.User;

import lombok.Value;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login {
    final static String HTTPS = "localhost:8080";
    public User login(String email, String username, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthServices authServices = retrofit.create(AuthServices.class);

        Call<User> call = authServices.login();
        final User[] user = {new User()};
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user[0] = response.body();
                    Log.d("User", "User name: " + user.getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("User", "Network error: " + t.getMessage());
            }
        });
        return user[0];
    }
}
