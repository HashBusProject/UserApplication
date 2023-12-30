package com.hashimte.hashbus1.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Ticket {
    @SerializedName("id")
    private Integer id;
    @SerializedName("journey")
    private Journey journey;
    @SerializedName("user")
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
