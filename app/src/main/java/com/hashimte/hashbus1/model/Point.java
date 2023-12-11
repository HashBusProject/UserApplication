package com.hashimte.hashbus1.model;


import com.google.gson.annotations.SerializedName;

import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//It's Working Don't Touch
public class Point {
    @SerializedName("id")
    private Integer id;
    @SerializedName("pointName")
    private String pointName;
    @SerializedName("x")
    private Double x;
    @SerializedName("y")
    private Double y;
    //    private HashSet<Journey> journeys;
    @SerializedName("journeysID")
    private HashSet<Integer> journeysID;
    public Point(Point point) {
        this.pointName = point.getPointName();
    }

    public String getPointName() {
        return pointName;
    }
}