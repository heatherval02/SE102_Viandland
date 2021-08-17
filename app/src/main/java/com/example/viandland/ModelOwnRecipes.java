package com.example.viandland;

public class ModelOwnRecipes {

    int recipe_id;
    String recipe_name, recipe_image;

    public ModelOwnRecipes(int recipe_id, String recipe_name, String recipe_image) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.recipe_image = recipe_image;
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

    public String getRecipe_image() {
        return recipe_image;
    }

    public void setRecipe_image(String recipe_image) {
        this.recipe_image = recipe_image;
    }
}
