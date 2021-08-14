package com.example.viandland;

public class ModelInstructionList {

    int instructionId, recipeId;
    String instructionString;

    public ModelInstructionList(int instructionId, int recipeId, String instructionString) {
        this.instructionId = instructionId;
        this.recipeId = recipeId;
        this.instructionString = instructionString;
    }

    public int getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(int instructionId) {
        this.instructionId = instructionId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getInstructionString() {
        return instructionString;
    }

    public void setInstructionString(String instructionString) {
        this.instructionString = instructionString;
    }
}
