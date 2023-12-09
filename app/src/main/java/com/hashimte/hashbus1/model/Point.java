package com.hashimte.hashbus1.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//It's Working Don't Touch
public class Point {
    private long id;
    private String pointName;
    private double x;
    private double y;
    public Point(Point point) {
        this.pointName = point.getPointName();
    }
}