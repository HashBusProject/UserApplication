package com.hashimte.hashbus1.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ChangePassword {
    @SerializedName("user")
    private User user;

    @SerializedName("newPassword")
    private String newPassword ;

    public ChangePassword(User user, String newPassword) {
        this.user = user;
        this.newPassword = newPassword;
    }
}
