package com.hashimte.hashbus1.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
//It's Working Don't Touch
public class Point {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("pointName")
    @Expose
    private String pointName;
    @Expose
    @SerializedName("x")
    private Double x;
    @Expose
    @SerializedName("y")
    private Double y;
    @Expose
    @SerializedName("journeysID")
    private HashSet<Integer> journeysID;
    public Point(Point point) {
        this.pointName = point.getPointName();
    }
    public Point(){}

    public Point(Integer id) {
        this.id = id;
    }

    public String getPointName() {
        return pointName;
    }
}