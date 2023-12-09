package com.hashimte.hashbus1.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class User {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("password")
    private String password;
    @SerializedName("role")
    private int role;
}
