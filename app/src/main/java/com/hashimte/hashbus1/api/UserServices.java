package com.hashimte.hashbus1.api;

import com.hashimte.hashbus1.model.Bus;
import com.hashimte.hashbus1.model.Journey;
import com.hashimte.hashbus1.model.Point;
import com.hashimte.hashbus1.model.SearchDataSchedule;
import com.hashimte.hashbus1.model.Ticket;
import com.hashimte.hashbus1.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    Call<Ticket> getTicketsByUserId(@Query("userId") Integer userId);

    @GET("/User/GetAllJournys")
    Call<List<Journey>> getAllJourneys();

    @GET("/User/GetJourneyById")
    Call<Journey> getJourneyById(@Query("journeyId") Integer journeyId);

    @GET("/User/BuyTicket")
    Call<Boolean> buyATicket();

    @GET("/User/AllPointByJourneyId")
    Call<List<Point>> getAllPointByJourneyId(@Query("journeyId") Integer journeyId);

}
