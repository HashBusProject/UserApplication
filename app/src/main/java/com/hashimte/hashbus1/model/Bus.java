package com.hashimte.hashbus1.model;

import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bus {
    private User driver;
    private long id;
    private int cap;
    private HashSet<Schedule> schedules;
    private boolean isWorking;
}
