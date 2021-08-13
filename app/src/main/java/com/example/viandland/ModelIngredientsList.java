package com.example.viandland;

public class ModelIngredientsList {

    int ingredientsId, recipeId;
    String ingredientsString;

    public ModelIngredientsList(int ingredientsId, int recipeId, String ingredientsString) {
        this.ingredientsId = ingredientsId;
        this.recipeId = recipeId;
        this.ingredientsString = ingredientsString;
    }

    public int getIngredientsId() {
        return ingredientsId;
    }

    public void setIngredientsId(int ingredientsId) {
        this.ingredientsId = ingredientsId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getIngredientsString() {
        return ingredientsString;
    }

    public void setIngredientsString(String ingredientsString) {
        this.ingredientsString = ingredientsString;
    }
}
