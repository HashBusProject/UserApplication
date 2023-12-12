package com.hashimte.hashbus1.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Journey {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("sourcePoint")
    private Integer sourcePoint;
    @SerializedName("destinationPoint")
    private Integer destinationPoint;
    @SerializedName("stopPoints")
    private HashSet<Integer> stopPoints;
    @SerializedName("journeys")
    private HashSet<Schedule> journeys;
}
