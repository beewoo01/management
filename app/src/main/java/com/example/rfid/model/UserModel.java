package com.example.rfid.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;


@Data
public class UserModel implements Serializable {

    @SerializedName("accountidx")
    @Expose
    public Integer accountIdx;

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("phone")
    @Expose
    public String phone;

    @SerializedName("agree1")
    @Expose
    public boolean agree1;

    @SerializedName("agree2")
    @Expose
    public boolean agree2;

    @SerializedName("agree3")
    @Expose
    public boolean agree3;


    @SerializedName("historyidx")
    @Expose
    public Integer historyIdx;

    @SerializedName("adminidx")
    @Expose
    public int adminIdx;

    @SerializedName("adminid")
    @Expose
    public String adminId;

    @SerializedName("colorcode")
    @Expose
    public String colorCode;

    @SerializedName("useridx")
    @Expose
    public int userIdx;

    @SerializedName("userid")
    @Expose
    public String userId;

    @SerializedName("createdate")
    @Expose
    public String createDate;

    @SerializedName("history_date")
    @Expose
    public String historyDate;

    @SerializedName("updatedate")
    @Expose
    public String updateDate;

}
