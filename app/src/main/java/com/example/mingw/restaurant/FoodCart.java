package com.example.mingw.restaurant;

import org.litepal.crud.DataSupport;


public class FoodCart extends DataSupport{

    private String foodname;
    private String imgUrl;
    private int foodnumber;
    private double foodprice;
    private String status;


    public String getFoodname() {
        return foodname;
    }


    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }


    public String getImgUrl() {
        return imgUrl;
    }


    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public int getFoodnumber() {
        return foodnumber;
    }


    public void setFoodnumber(int foodnumber) {
        this.foodnumber = foodnumber;
    }


    public double getFoodprice() {
        return foodprice;
    }


    public void setFoodprice(double foodprice) {
        this.foodprice = foodprice;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }
}
