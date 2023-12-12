package com.hashimte.hashbus1.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;

import lombok.Data;

@Data
public class Schedule {
    @SerializedName("journey")
    private Integer journey; // 1 - 9
    @SerializedName("bus")
    private Integer bus;   // 1  2  3  4  5
    @SerializedName("time")
    private String time; // 10 11 12 13 18

    public String getTime() {
        return time;
    }
}
