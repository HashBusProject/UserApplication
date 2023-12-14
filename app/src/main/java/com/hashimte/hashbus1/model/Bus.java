package com.hashimte.hashbus1.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bus {
    @SerializedName("driver")
    private User driver;
    @SerializedName("id")
    private Integer id;
    @SerializedName("cap")
    private Integer cap;
    @SerializedName("schedules")
    private HashSet<Schedule> schedules;
    @SerializedName("isWorking")
    private Boolean isWorking;
    @SerializedName("x")
    private Double x;
    @SerializedName("y")
    private Double y;
}
