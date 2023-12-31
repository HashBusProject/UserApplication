package com.hashimte.hashbus1.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchDataSchedule {
    @SerializedName("journey")
    private Journey journey;
    @SerializedName("schedule")
    @Expose
    private Schedule schedule;
    @SerializedName("bus")
    private Bus bus;

    public Bus getBus() {
        return bus;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
}
