package com.example.primero.Models;

public class SlotModel {

    String time;
    String price;
    String status;

    public SlotModel(String time, String price, String status) {
        this.time = time;
        this.price = price;
        this.status = status;
    }

    public SlotModel(String time, String price) {
        this.time = time;
        this.price = price;
    }

    public SlotModel() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
