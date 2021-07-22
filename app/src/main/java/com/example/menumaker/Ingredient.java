package com.example.menumaker;

/**
 * Created by llconnor on 9/12/2017.
 */

public class Ingredient {
    private String mIngredientName;
    private int mCount;

    Ingredient(String ingredient) {
        mIngredientName = ingredient;
        mCount = 1;
    }

    @Override
    public String toString() {
        return mIngredientName;
    }

    public String toStringWithCount() {return mIngredientName + " " + String.valueOf(mCount);}

    public void IncreaseCount(){
        mCount++;
    }

    public boolean equals(Ingredient toCompare) {
        if (mCount != toCompare.mCount) {
            return false;
        }
        return mIngredientName.equals(toCompare.mIngredientName);
    }

    public  int getCount() {return mCount;}
}
