package com.example.menumaker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by llconnor on 9/9/2017.
 */

public class RecipeList implements Parcelable {
    private final ArrayList<Recipe> mRecipeList;

    RecipeList() {
        mRecipeList = new ArrayList<>();
    }

    RecipeList(Parcel inputParcel) {
        // *** TODO: I don't like the copy/paste code here.  Is there a way I can reuse the Parcel constructor in Recipe?
        mRecipeList = new ArrayList<>();
        String recipeName = inputParcel.readString();
        ArrayList<String> readALS = new ArrayList<>();
        while (recipeName != null) {
            inputParcel.readStringList(readALS);
            mRecipeList.add(new Recipe(recipeName,readALS));
            recipeName = inputParcel.readString();
        }
    }

    RecipeList (ArrayList<Recipe> inRecipeList) {
        mRecipeList = inRecipeList;
    }

    RecipeList(RecipeList deepCopy) {
        mRecipeList = new ArrayList<>();
        for (int i = 0; i < deepCopy.Length(); i++) {
            Recipe tempRecipe = deepCopy.getRecipeAtPosition(i);
            addRecipe(tempRecipe);
        }
    }

    public static final Creator<RecipeList> CREATOR = new Creator<RecipeList>() {
        @Override
        public RecipeList createFromParcel(Parcel in) {
            return new RecipeList(in);
        }

        @Override
        public RecipeList[] newArray(int size) {
            return new RecipeList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        for (Recipe recipe:mRecipeList) {
            recipe.writeToParcel(dest, flags);
        }
    }

    public boolean equals(RecipeList toCompare) {
        for (Recipe recipe:mRecipeList) {
            boolean match = false;
            for (Recipe compRecipe:toCompare.mRecipeList) {
                if (recipe.equals(compRecipe)) {
                    match = true;
                }
            }
            if (!match) {
                return false;
            }
        }
        return true;
    }

    static class RecipeListException extends Exception
    {
        public RecipeListException(String message) {
            super(message);
        }
    }

    public String toString() {
        StringBuilder recipeListStr = new StringBuilder();
        for (Recipe recipe:mRecipeList) {
            recipeListStr.append(recipe.getRecipeName()).append("\n");
            recipeListStr.append(recipe.getIngredientList().IngredientListToString()).append("\n");
        }
        return recipeListStr.toString();
    }

    public int size() {
        return mRecipeList.size();
    }

    public void ReadRecipeFile(MainActivity context) throws RecipeListException {
        Resources resource = context.getResources();
        TypedArray typedArray = resource.obtainTypedArray(R.array.Recipes);
        int recipeLen = typedArray.length();
        String[][] ingredients = new String[recipeLen][];
        for (int i = 0; i < recipeLen; ++i) {
            int id = typedArray.getResourceId(i, 0);
            if (id > 0) {
                ingredients[i] = resource.getStringArray(id);
                String recipeName = ingredients[i][0];
                if (ingredients.length == 1) {
                    mRecipeList.add(new Recipe(recipeName));
                } else {
                    String[] subIng = Arrays.copyOfRange(ingredients[i], 1, ingredients[i].length);
                    mRecipeList.add(new Recipe(recipeName, subIng));
                }
            } else {
                throw(new RecipeListException("Ingredient XML is empty or malformed."));
            }
        }
        typedArray.recycle();
    }

    public ArrayList<String> ListOfRecipeNames(){
        ArrayList<String> retALS = new ArrayList<>();
        for (Recipe recipe:mRecipeList)
        {
            retALS.add(recipe.getRecipeName());
        }
        return retALS;
    }

    public ArrayList<String> GroceryList() {
        IngredientList GroceryList = new IngredientList();
        for (Recipe recipe:mRecipeList) {
            GroceryList.mergeIngredientLists(recipe.getIngredientList());
        }
        return GroceryList.getIngredientsAndCountAsStringArrayList();
    }

    public int Length() {
        return mRecipeList.size();
    }

    public Recipe getRecipeAtPosition(int position) {
        return mRecipeList.get(position);
    }

    public int getNumRecipes() {
        return mRecipeList.size();
    }

    public RecipeList makeMenu(int numRecipes)
    {
        RecipeList tempRecipeList = new RecipeList(this);
        RecipeList menuRecipeList = new RecipeList();
        Random r = new Random();
        // If we want more recipes then we have then we'll just return the list (implicitly)
        while(tempRecipeList.Length() > 0 && numRecipes > 0)
        {
            int nextChoice = r.nextInt(tempRecipeList.Length());
            menuRecipeList.addRecipe(tempRecipeList.getRecipeAtPosition(nextChoice));
            tempRecipeList.RemoveRecipe(nextChoice);
            numRecipes--;
        }
        return menuRecipeList;
    }

    private void RemoveRecipe(int nextChoice) {
        this.mRecipeList.remove(nextChoice);
    }

    private void addRecipe(Recipe newRecipe) {
        this.mRecipeList.add(newRecipe);
    }

    public void WriteToFile (String filename, Context context) {
        StringBuilder buffer = new StringBuilder();
        File fileHandle = new File(context.getFilesDir() + filename);
        try {
            PrintWriter pout = new PrintWriter(new FileOutputStream(fileHandle));
            for (Recipe recipe:this.mRecipeList) {
                buffer.append(recipe.toFileStr());
            }
            pout.print(buffer);
            pout.close();
        }
        catch (FileNotFoundException e) {
            Toast.makeText(context, "Warning...Menu could not be saved", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.d("WriteToFile", e.toString());
        }
    }

    public void ReadFromFile (String filename, Context context) {
        try {
            String content = new Scanner(new File(context.getFilesDir() + filename))
                    .useDelimiter("\\Z").next();
            String [] lineList = content.split("\n");
            for (String line:lineList) {
                String [] recipeList = line.split(",");
                String recipeName = recipeList[0];
                ArrayList<String> ingList = new ArrayList<>(Arrays.asList(recipeList).subList(1, recipeList.length));
                addRecipe(new Recipe(recipeName, ingList));
            }
        }
        catch (FileNotFoundException e) {
            // We don't actually need to do anything as the RecipeList will be null
            Toast.makeText(context, "Warning...Menu could not be loaded", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.d("ReadFromFile", e.toString());
        }
    }
}
