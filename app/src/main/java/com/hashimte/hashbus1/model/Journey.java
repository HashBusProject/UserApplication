package com.hashimte.hashbus1.model;

import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Journey {
    private long id;
    private String name;
    private Point sourcePoint;
    private Point destinationPoint;
    private HashSet<Point> stopPoints;
    private HashSet<Schedule> journeys;
}
