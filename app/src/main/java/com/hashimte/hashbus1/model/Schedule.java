package com.hashimte.hashbus1.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Time;

import lombok.Data;

@Data
public class Schedule {
    @SerializedName("scheduleId")
    @Expose
    private Integer scheduleId;
    @SerializedName("journey")
    private Integer journey; // 1 - 9
    @SerializedName("bus")
    private Integer bus;   // 1  2  3  4  5
    @SerializedName("time")
    private String time; // 10 11 12 13 18
    @SerializedName("nextPoint")
    private Integer nextPoint;
    @SerializedName("passengersNumber")
    private Integer passengersNumber;

    public String getTime() {
        return time;
    }

    public Integer getJourney() {
        return journey;
    }

    public void setJourney(Integer journey) {
        this.journey = journey;
    }

    public Integer getBus() {
        return bus;
    }

    public void setBus(Integer bus) {
        this.bus = bus;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getNextPoint() {
        return nextPoint;
    }

    public void setNextPoint(Integer nextPoint) {
        this.nextPoint = nextPoint;
    }

    public Integer getPassengersNumber() {
        return passengersNumber;
    }

    public void setPassengersNumber(Integer passengersNumber) {
        this.passengersNumber = passengersNumber;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }
}
