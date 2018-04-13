package com.example.mingw.restaurant;

public class Food {
    private int id;
    private String name;
    private String description;
    private String imgUrl;
    private double price;
    public Food(int id, String name, String description, String imgUrl, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getImgUrl() {
        return imgUrl;
    }


    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public double getPrice() {
        return price;
    }


    public void setPrice(double price) {
        this.price = price;
    }
}
