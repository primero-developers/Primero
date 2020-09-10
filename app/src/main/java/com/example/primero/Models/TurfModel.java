package com.example.primero.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "turf")
public class TurfModel {

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
    @ColumnInfo(name = "coordinates")
    public ArrayList<String> coordinates;
    @ColumnInfo(name = "turfPhone")
    String turfPhone;
    @ColumnInfo(name = "slots")
    public ArrayList<String> slots;
    @ColumnInfo(name = "slotsPrice")
    public ArrayList<String> slotsPrice;
    @ColumnInfo(name = "imgUrl")
    String imgUrl;
    @ColumnInfo(name = "turfDesc")
    String turfDesc;
    @ColumnInfo(name = "turfMapUrl")
    String turfMapUrl;


    public TurfModel() {
    }

    @Ignore
    public TurfModel(@NonNull String turfId, String turfName, String turfLocation, String turfSize, ArrayList<String> coordinates, String turfPhone, ArrayList<String> slots, ArrayList<String> slotsPrice, String imgUrl, String turfDesc, String turfMapUrl) {
        this.turfId = turfId;
        this.turfName = turfName;
        this.turfLocation = turfLocation;
        this.turfSize = turfSize;
        this.coordinates = coordinates;
        this.turfPhone = turfPhone;
        this.slots = slots;
        this.slotsPrice = slotsPrice;
        this.imgUrl = imgUrl;
        this.turfDesc = turfDesc;
        this.turfMapUrl = turfMapUrl;
    }







    public String getTurfId() {
        return turfId;
    }

    public void setTurfId(String turfId) {
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

    public ArrayList<String> getSlots() {
        return slots;
    }

    public void setSlots(ArrayList<String> slots) {
        this.slots = slots;
    }

    public ArrayList<String> getSlotPrice() {
        return slotsPrice;
    }

    public void setSlotPrice(ArrayList<String> slotPrice) {
        this.slotsPrice = slotPrice;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTurfSize() {
        return turfSize;
    }

    public void setTurfSize(String turfSize) {
        this.turfSize = turfSize;
    }

    public String getTurfDesc() {
        return turfDesc;
    }

    public void setTurfDesc(String turfDesc) {
        this.turfDesc = turfDesc;
    }

    public String getTurfMapUrl() {
        return turfMapUrl;
    }

    public void setTurfMapUrl(String turfMapUrl) {
        this.turfMapUrl = turfMapUrl;
    }

    public ArrayList<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<String> coordinates) {
        this.coordinates = coordinates;
    }

    public String getTurfPhone() {
        return turfPhone;
    }

    public void setTurfPhone(String turfPhone) {
        this.turfPhone = turfPhone;
    }
}
