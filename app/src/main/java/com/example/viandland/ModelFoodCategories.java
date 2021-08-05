package com.example.viandland;

public class ModelFoodCategories {

    private int image;
    private String text;

    public ModelFoodCategories(int image, String text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
