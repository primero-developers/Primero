package com.example.primero.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "user")
public class UserModel {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "uId")
    public String uId;
    @ColumnInfo(name = "phone")
    public String phone;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "profileImg")
    public String profileImg;


    @Ignore
    public UserModel(String uId, String phone, String name, String profileImg) {
        this.uId = uId;
        this.phone = phone;
        this.name = name;
        this.profileImg =profileImg;
    }

    public UserModel() {
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

}
