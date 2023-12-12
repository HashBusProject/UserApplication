package com.hashimte.hashbus1.api;

import com.google.gson.Gson;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.SearchDataSchedule;
import com.hashimte.hashbus1.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public class UserServicesImp implements UserServices {

    private final static String HTTPS1 = "https://global-memento-407716.uc.r.appspot.com/";

    private final static UserServicesImp instance = new UserServicesImp();

    private Retrofit retrofit = getRetrofit();

    private UserServicesImp() {

    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(HTTPS1)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public static UserServicesImp getInstance() {
        return instance;
    }

    @Override
    @GET("/User/GetAllPoint")
    public Call<List<Point>> getAllPoints() {
        return retrofit.create(UserServices.class).getAllPoints();
    }

    @Override
    @GET("/User/GetScheduleByPointsAndTime")
    public Call<List<SearchDataSchedule>> getSearchDataSchedule(
            @Query("startPointId") Integer startPoint,
            @Query("endPointId") Integer endPoint,
            @Query("time") String time) {
        return retrofit.create(UserServices.class).getSearchDataSchedule(startPoint, endPoint, time);
    }
}
