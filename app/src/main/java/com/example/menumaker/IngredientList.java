package com.example.menumaker;

import java.util.ArrayList;

/**
 * Created by llconnor on 10/4/2017.
 */

public class IngredientList{
    private final ArrayList<Ingredient> mIngredients;

    IngredientList() {
        mIngredients = new ArrayList<>();
    }

    IngredientList(String[] ingredientArray) {
        mIngredients = new ArrayList<>();
        for (String s : ingredientArray) {
            mIngredients.add(new Ingredient(s));
        }
    }

    IngredientList(ArrayList<String> ingredientList) {
        mIngredients = new ArrayList<>();
        for (String ing: ingredientList)
        {
            mIngredients.add(new Ingredient(ing));
        }
    }


    public void AddIngredient(Ingredient newIng) {
        mIngredients.add(newIng);
    }

    public ArrayList<String> getIngredientsAsStringArrayList() {
        ArrayList<String> retALS = new ArrayList<>();
        for(Ingredient ingredient:mIngredients) {
            retALS.add(ingredient.toString());
        }
        return retALS;
    }

    public ArrayList<String> getIngredientsAndCountAsStringArrayList() {
        ArrayList<String> retALS = getIngredientsAsStringArrayList();
        for (int i = 0; i < mIngredients.size(); i++) {
            int count = mIngredients.get(i).getCount();
            retALS.set(i,retALS.get(i) + " " + count);
        }
        return retALS;
    }

    public String IngredientListToString() {
        StringBuilder ingStr = new StringBuilder();
        for(Ingredient ing:mIngredients){
            ingStr.append("\t\t\t\t").append(ing.toString()).append("\n");
        }
        return ingStr.toString();
    }

    IngredientList mergeIngredientLists(IngredientList listToMerge) {
        for (Ingredient inIngredient:listToMerge.mIngredients) {
            boolean foundIng = false;
            for (Ingredient ingredient:mIngredients) {
                if (inIngredient.equals(ingredient)) {
                    ingredient.IncreaseCount();
                    foundIng = true;
                    break;
                }
            }
            if(!foundIng) {
                mIngredients.add(inIngredient);
            }
        }
        return this;
    }

    Boolean equals(IngredientList toCompare) {
        if (mIngredients.size() != toCompare.mIngredients.size()) {
            return false;
        }
        for (int i = 0; i < mIngredients.size(); i++) {
            if (!mIngredients.get(i).equals(toCompare.mIngredients.get(i))) {
                return false;
            }
        }
        return true;
    }

    public Ingredient getIngredientAtPosition(int position) {
        return mIngredients.get(position);
    }

    ArrayList<Ingredient> getIngredientList() {
        return mIngredients;
    }
}
