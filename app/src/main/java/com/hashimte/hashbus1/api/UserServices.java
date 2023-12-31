package com.hashimte.hashbus1.api;

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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface UserServices {
    @GET("/User/GetAllPoint")
    Call<List<Point>> getAllPoints();

    @GET("/User/GetScheduleByPointsAndTime")
    Call<List<SearchDataSchedule>> getSearchDataSchedule(
            @Query("startPointId") Integer startPoint,
            @Query("endPointId") Integer endPoint,
            @Query("time") String time);

    @GET("/User/GetPointById")
    Call<Point> getPointByID(@Query("pointId") Integer pointId);

    @GET("/User/GetBusById")
    Call<Bus> getBusById(@Query("busId") Integer busId);

    @GET("/User/GetTicketsByUserId")
    Call<List<Ticket>> getTicketsByUserId(@Query("userId") Integer userId);

    @GET("/User/GetAllJournys")
    Call<List<Journey>> getAllJourneys();

    @GET("/User/GetJourneyById")
    Call<Journey> getJourneyById(@Query("journeyId") Integer journeyId);

    @POST("/User/BuyTicket")
    Call<Boolean> buyATicket(@Query("userId") Integer userId, @Query("journeyId") Integer journeyId);

    @GET("/User/AllPointByJourneyId")
    Call<List<Point>> getAllPointByJourneyId(@Query("journeyId") Integer journeyId);

    @POST("/User/ConfirmRide")
    Call<Boolean> confirmRide(
            @Query("user") Integer userId,
            @Query("journey") Integer journeyId,
            @Query("bus") Integer busId,
            @Body Schedule schedule);

    @POST("/User/CancelReserve")
    Call<Boolean> cancelReserve(@Query("scheduleID") Integer scheduleID);


    @POST("/User/ReserveASite")
    Call<Boolean> reserveASite(@Query("scheduleID") Integer scheduleID);

    @PUT("/User/ChangeEmail")
    Call<Boolean> changeEmail(@Body User user);

    @PUT("/User/ChangePassword")
    Call<Boolean> changePassword(@Body ChangePassword changePassword) ;
}
