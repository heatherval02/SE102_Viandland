package com.example.viandland;

import android.content.Context;

public class ModelWhatsNew {

    int recipe_id;
    String recipe_name,recipe_date_added, recipe_description, recipe_image, cook_image, cook_name;

    public ModelWhatsNew(int recipe_id, String recipe_name, String recipe_date_added, String recipe_description, String recipe_image, String cook_image, String cook_name) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.recipe_date_added = recipe_date_added;
        this.recipe_description = recipe_description;
        this.recipe_image = recipe_image;
        this.cook_image = cook_image;
        this.cook_name = cook_name;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getRecipe_date_added() {
        return recipe_date_added;
    }

    public void setRecipe_date_added(String recipe_date_added) {
        this.recipe_date_added = recipe_date_added;
    }

    public String getRecipe_description() {
        return recipe_description;
    }

    public void setRecipe_description(String recipe_description) {
        this.recipe_description = recipe_description;
    }

    public String getRecipe_image() {
        return recipe_image;
    }

    public void setRecipe_image(String recipe_image) {
        this.recipe_image = recipe_image;
    }

    public String getCook_image() {
        return cook_image;
    }

    public void setCook_image(String cook_image) {
        this.cook_image = cook_image;
    }

    public String getCook_name() {
        return cook_name;
    }

    public void setCook_name(String cook_name) {
        this.cook_name = cook_name;
    }
}
