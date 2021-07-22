package com.example.menumaker;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by llconnor on 10/4/2017.
 */

public class IngredientList{
    private ArrayList<Ingredient> mIngredients;

    IngredientList() {
        mIngredients = new ArrayList<>();
    }

    IngredientList(String[] ingredientArray) {
        mIngredients = new ArrayList<Ingredient>();
        for(int i = 0; i < ingredientArray.length; i++) {
            mIngredients.add(new Ingredient(ingredientArray[i]));
        }
    }

    IngredientList(ArrayList<String> ingredientList) {
        mIngredients = new ArrayList<Ingredient>();
        for (String ing: ingredientList)
        {
            mIngredients.add(new Ingredient(ing));
        }
    }


    public void AddIngredient(Ingredient newIng) {
        mIngredients.add(newIng);
    }

    public ArrayList<String> getIngredientsAsStringArrayList() {
        ArrayList<String> retALS = new ArrayList<String>();
        for(Ingredient ingredient:mIngredients) {
            retALS.add(ingredient.toString());
        }
        return retALS;
    }

    public ArrayList<String> getIngredientsAndCountAsStringArrayList() {
        ArrayList<String> retALS = getIngredientsAsStringArrayList();
        for (int i = 0; i < mIngredients.size(); i++) {
            int count = mIngredients.get(i).getCount();
            retALS.set(i,retALS.get(i) + " " + String.valueOf(count));
        }
        return retALS;
    }

    public String IngredientListToString() {
        String ingStr = "";
        for(Ingredient ing:mIngredients){
            ingStr += "\t\t\t\t" + ing.toString() + "\n";
        }
        return ingStr;
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
            if(foundIng == false) {
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
            if (mIngredients.get(i).equals(toCompare.mIngredients.get(i)) == false) {
                return false;
            }
        }
        return true;
    }

    ArrayList<Ingredient> getIngredientList() {
        return mIngredients;
    }
}
