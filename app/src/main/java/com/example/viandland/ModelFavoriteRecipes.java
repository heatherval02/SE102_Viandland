package com.example.viandland;

public class ModelFavoriteRecipes {
    int recipe_id;
    String favorites_id, recipe_name, recipe_description, recipe_date_added, recipe_cook, recipe_prep_time, recipe_image, category, table_name;

    public ModelFavoriteRecipes(int recipe_id, String favorites_id, String recipe_name, String recipe_description, String recipe_date_added, String recipe_cook, String recipe_prep_time, String recipe_image, String category, String table_name) {
        this.recipe_id = recipe_id;
        this.favorites_id = favorites_id;
        this.recipe_name = recipe_name;
        this.recipe_description = recipe_description;
        this.recipe_date_added = recipe_date_added;
        this.recipe_cook = recipe_cook;
        this.recipe_prep_time = recipe_prep_time;
        this.recipe_image = recipe_image;
        this.category = category;
        this.table_name = table_name;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getFavorites_id() {
        return favorites_id;
    }

    public void setFavorites_id(String favorites_id) {
        this.favorites_id = favorites_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getRecipe_description() {
        return recipe_description;
    }

    public void setRecipe_description(String recipe_description) {
        this.recipe_description = recipe_description;
    }

    public String getRecipe_date_added() {
        return recipe_date_added;
    }

    public void setRecipe_date_added(String recipe_date_added) {
        this.recipe_date_added = recipe_date_added;
    }

    public String getRecipe_cook() {
        return recipe_cook;
    }

    public void setRecipe_cook(String recipe_cook) {
        this.recipe_cook = recipe_cook;
    }

    public String getRecipe_prep_time() {
        return recipe_prep_time;
    }

    public void setRecipe_prep_time(String recipe_prep_time) {
        this.recipe_prep_time = recipe_prep_time;
    }

    public String getRecipe_image() {
        return recipe_image;
    }

    public void setRecipe_image(String recipe_image) {
        this.recipe_image = recipe_image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }
}
