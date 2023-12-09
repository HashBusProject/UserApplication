package com.hashimte.hashbus1.model;

import java.sql.Time;

import lombok.Data;

@Data
public class Schedule {
    private Journey journey; // 1 - 9
    private Bus bus;   // 1  2  3  4  5
    private Time time; // 10 11 12 13 18
}
