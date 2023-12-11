package com.hashimte.hashbus1.api;

import com.hashimte.hashbus1.model.Point;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserServices {
    @GET("/User/GetAllPoint")
    Call<List<Point>> getAllPoints();
}
