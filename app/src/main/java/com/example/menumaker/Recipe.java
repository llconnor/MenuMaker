package com.example.menumaker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by llconnor on 9/9/2017.
 */

public class Recipe implements Parcelable {
    //private ArrayList<Ingredient> mIngredients;
    private final IngredientList mIngredients;
    private String mRecipeName;

    Recipe (String recipeName)
    {
        mRecipeName = recipeName;
        mIngredients = new IngredientList();
    }

    Recipe (String recipeName, String[] ingredientArray) {
        mRecipeName = recipeName;
        mIngredients = new IngredientList(ingredientArray);
    }

    Recipe (String recipeName, ArrayList<String> ingredientList) {
        mRecipeName = recipeName;
        mIngredients = new IngredientList(ingredientList);
    }

    protected Recipe(Parcel in) {
        mRecipeName = in.readString();
        mIngredients = new IngredientList();
        ArrayList<String> readALS = new ArrayList<>();
        in.readStringList(readALS);
        for (String ing: readALS)
        {
            mIngredients.AddIngredient(new Ingredient(ing));
        }
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getRecipeName() {
        return mRecipeName;
    }

    public void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }

    public void setIngredients(String [] ingredients){
        for(String ingredient:ingredients){
            mIngredients.AddIngredient(new Ingredient(ingredient));
        }
    }

    public IngredientList getIngredientList() {
        return mIngredients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRecipeName);
        dest.writeStringList(mIngredients.getIngredientsAsStringArrayList());
    }

    @Override
    public String toString() {
        return mRecipeName + "\n" + mIngredients.IngredientListToString();
    }

    public String toFileStr() {
        StringBuilder recipeStr = new StringBuilder(mRecipeName);
        for (String ing:mIngredients.getIngredientsAsStringArrayList()) {
            recipeStr.append(",").append(ing);
        }
        return recipeStr + "\n";
    }

    public boolean equals(Recipe toCompare) {
        if (!this.mRecipeName.equals(toCompare.mRecipeName)) {
            return false;
        }
        return this.mIngredients.equals(toCompare.mIngredients);
    }
}