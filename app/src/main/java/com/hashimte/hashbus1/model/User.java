package com.hashimte.hashbus1.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/*
org.jetbrains.kotlin.plugin.serialization'
 */
@Data
public class User {
    @SerializedName("userID")
    private Integer userID;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("password")
    private String password;
    @SerializedName("role")
    private Integer role;
}
