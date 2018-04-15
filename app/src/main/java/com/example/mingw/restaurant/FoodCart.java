package com.example.mingw.restaurant;

import org.litepal.crud.DataSupport;

public class FoodCart extends DataSupport{

    private String name;
    private String imgUrl;
    private int number;
    private double price;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getImgUrl() {
        return imgUrl;
    }


    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public int getNumber() {
        return number;
    }


    public void setNumber(int number) {
        this.number = number;
    }


    public double getPrice() {
        return price;
    }


    public void setPrice(double price) {
        this.price = price;
    }
}
