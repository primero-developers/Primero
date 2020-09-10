package com.example.primero.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favTurf")
public class FavModel {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "turfId")
    String turfId;
    @ColumnInfo(name = "turfName")
    String turfName;
    @ColumnInfo(name = "turfLocation")
    String turfLocation;
    @ColumnInfo(name= "turfSize")
    String turfSize;
    @ColumnInfo(name = "imgUrl")
    String imgUrl;

    public FavModel() {
    }

    @Ignore
    public FavModel(@NonNull String turfId, String turfName, String turfLocation, String turfSize, String imgUrl) {
        this.turfId = turfId;
        this.turfName = turfName;
        this.turfLocation = turfLocation;
        this.turfSize = turfSize;
        this.imgUrl = imgUrl;
    }

    @NonNull
    public String getTurfId() {
        return turfId;
    }

    public void setTurfId(@NonNull String turfId) {
        this.turfId = turfId;
    }

    public String getTurfName() {
        return turfName;
    }

    public void setTurfName(String turfName) {
        this.turfName = turfName;
    }

    public String getTurfLocation() {
        return turfLocation;
    }

    public void setTurfLocation(String turfLocation) {
        this.turfLocation = turfLocation;
    }

    public String getTurfSize() {
        return turfSize;
    }

    public void setTurfSize(String turfSize) {
        this.turfSize = turfSize;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
