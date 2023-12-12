package com.hashimte.hashbus1.api;

import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.SearchDataSchedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface UserServices {
    @GET("/User/GetAllPoint")
    Call<List<Point>> getAllPoints();

    @GET("/User/GetScheduleByPointsAndTime")
    Call<List<SearchDataSchedule>> getSearchDataSchedule(
            @Query("startPointId") Integer startPoint,
            @Query("endPointId") Integer endPoint,
            @Query("time") String time);
}
