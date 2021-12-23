package com.ijiwon.convindex.tools;

import androidx.annotation.NonNull;

public class ProductClass {

    private String image;
    private String name;
    private String price;
    private String detail_info;
    private String short_info;
    private String diet;
    private String calories;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetail_info() {
        return detail_info;
    }

    public void setDetail_info(String detail_info) {
        this.detail_info = detail_info;
    }

    public String getShort_info() {
        return short_info;
    }

    public void setShort_info(String short_info) {
        this.short_info = short_info;
    }


    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", detail_info='" + detail_info + '\'' +
                ", short_info='" + short_info + '\'' +
                ", diet=" + diet +
                ", calories='" + calories + '\'' +
                '}';
    }
}
