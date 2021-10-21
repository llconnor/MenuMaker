package com.example.menumaker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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
            mRecipeList.add(new Recipe(recipeName, readALS));
            recipeName = inputParcel.readString();
        }
    }

    RecipeList(ArrayList<Recipe> inRecipeList) {
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
        for (Recipe recipe : mRecipeList) {
            recipe.writeToParcel(dest, flags);
        }
    }

    public boolean equals(RecipeList toCompare) {
        for (Recipe recipe : mRecipeList) {
            boolean match = false;
            for (Recipe compRecipe : toCompare.mRecipeList) {
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

    static class RecipeListException extends Exception {
        public RecipeListException(String message) {
            super(message);
        }
    }

    public String toString() {
        StringBuilder recipeListStr = new StringBuilder();
        for (Recipe recipe : mRecipeList) {
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
                throw (new RecipeListException("Ingredient XML is empty or malformed."));
            }
        }
        typedArray.recycle();
    }

    public ArrayList<String> ListOfRecipeNames() {
        ArrayList<String> retALS = new ArrayList<>();
        for (Recipe recipe : mRecipeList) {
            retALS.add(recipe.getRecipeName());
        }
        return retALS;
    }

    public ArrayList<String> GroceryList() {
        IngredientList GroceryList = new IngredientList();
        for (Recipe recipe : mRecipeList) {
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

    public RecipeList makeMenu(int numRecipes) {
        RecipeList tempRecipeList = new RecipeList(this);
        RecipeList menuRecipeList = new RecipeList();
        Random r = new Random();
        // If we want more recipes then we have then we'll just return the list (implicitly)
        while (tempRecipeList.Length() > 0 && numRecipes > 0) {
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

    public void WriteToFile(String filename, Context context) {
        StringBuilder buffer = new StringBuilder();
        try (FileOutputStream fou = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fou);
            for (Recipe recipe : this.mRecipeList) {
                buffer.append(recipe.toFileStr());
            }
            outputStreamWriter.write(buffer.toString());
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
/*           Toast.makeText(context, "Warning...Menu could not be saved", Toast.LENGTH_LONG).show();
            Log.d("WriteToFile", e.toString());
        }
         */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: Why is this so fundamentally different than ReadRecipeFile (?)
    public void ReadFromFile(String filename, Context context) {
        try (FileInputStream fin = context.openFileInput(filename)) {
            InputStreamReader inputStreamReader = new InputStreamReader(fin);
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    // TODO: Add in error checking and or make this cleaner.
                    String recipeName = line.substring(0, line.indexOf(","));
                    line = line.substring(line.indexOf(",")+1);
                    String[] lineSplit = line.split(",");
                    Recipe tempRecipe = new Recipe(recipeName);
                    tempRecipe.setIngredients(lineSplit);
                    this.addRecipe(tempRecipe);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ReadFromFile", e.toString());
            }
        } catch (FileNotFoundException e) {
            // We don't actually need to do anything as the RecipeList will be null
            // TODO: Figure out how to update Toast
            //Toast.makeText(context, "Warning...Menu could not be loaded", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.d("ReadFromFile", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
