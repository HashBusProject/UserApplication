package com.hashimte.hashbus1.api;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.hashimte.hashbus1.model.Bus;
import com.hashimte.hashbus1.model.ChangePassword;
import com.hashimte.hashbus1.model.Journey;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.Schedule;
import com.hashimte.hashbus1.model.SearchDataSchedule;
import com.hashimte.hashbus1.model.Ticket;
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

public
class UserServicesImp implements UserServices {

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

    @Override
    @GET("/User/GetPointById")
    public Call<Point> getPointByID(@Query("pointId") Integer pointId) {
        return retrofit.create(UserServices.class).getPointByID(pointId);
    }

    @GET("/User/GetBusById")
    public Call<Bus> getBusById(@Query("busId") Integer busId) {
        return retrofit.create(UserServices.class).getBusById(busId);
    }

    @GET("/User/GetTicketsByUserId")
    public Call<List<Ticket>> getTicketsByUserId(@Query("userId") Integer userId) {
        return retrofit.create(UserServices.class).getTicketsByUserId(userId);
    }

    @GET("/User/GetAllJournys")
    public Call<List<Journey>> getAllJourneys() {
        return retrofit.create(UserServices.class).getAllJourneys();
    }

    @GET("/User/GetJourneyById")
    public Call<Journey> getJourneyById(@Query("journeyId") Integer journeyId) {
        return retrofit.create(UserServices.class).getJourneyById(journeyId);
    }

    @POST("/User/BuyTicket")
    public Call<Boolean> buyATicket(@Query("userId") Integer userId, @Query("journeyId") Integer journeyId) {
        return retrofit.create(UserServices.class).buyATicket(userId, journeyId);
    }

    @Override
    @GET("/User/AllPointByJourneyId")
    public Call<List<Point>> getAllPointByJourneyId(@Query("journeyId") Integer journeyId) {
        return retrofit.create(UserServices.class).getAllPointByJourneyId(journeyId);
    }

    @Override
    @POST("/User/ConfirmRide")
    public Call<Boolean> confirmRide(
            @Query("user") Integer userId,
            @Query("journey") Integer journeyId,
            @Query("bus") Integer busId,
            @Body Schedule schedule) {
        return retrofit.create(UserServices.class).confirmRide(userId, journeyId, busId, schedule);
    }

    @POST("/User/ReserveASite")
    public Call<Boolean> reserveASite(@Query("scheduleID") Integer scheduleID){
        return retrofit.create(UserServices.class).reserveASite(scheduleID);
    }

    @PUT("/User/ChangeEmail")
    public Call<Boolean> changeEmail(@Body User user) {
        return retrofit.create(UserServices.class).changeEmail(user);
    }

    @PUT("/User/ChangePassword")
    public Call<Boolean> changePassword(ChangePassword changePassword) {
        return retrofit.create(UserServices.class).changePassword(changePassword);
    }

    @POST("/User/CancelReserve")
    public Call<Boolean> cancelReserve(@Query("scheduleID") Integer scheduleID){
        return retrofit.create(UserServices.class).cancelReserve(scheduleID);
    }
}
