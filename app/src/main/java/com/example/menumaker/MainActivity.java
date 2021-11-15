package com.example.menumaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.menumaker.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

//TODO: Figure out how to get more recipes.  (ex: https://www.google.com/search?q=open+source+list+of+recipes&oq=open+source+list+of+recipes&aqs=chrome..69i57j69i64l3.5790j0j7&sourceid=chrome&ie=UTF-8)

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private RecipeList mCookbookList;
    private RecipeList mMenuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        LoadCookbook();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public RecipeList getCookBookList() {
        return mCookbookList;
    }

    public RecipeList getMenuList () {
        return mMenuList;
    }

    // Note: This should only ever be called if mMenuList is null
    public void loadMenu() {
        if (mMenuList == null) {
            String filename = getResources().getString(R.string.menu_outfile);
            mMenuList = new RecipeList();
            mMenuList.ReadFromFile(filename, getApplicationContext());
        }
    }

    public void buildMenuList () {
        mMenuList = mCookbookList.makeMenu(getMenuSizePreference());
    }

    public int getMenuSizePreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sizeKey = getString(R.string.pref_size_key);
        String sizeDefault = getString(R.string.pref_size_default);
        return Integer.parseInt(sharedPreferences.getString(sizeKey, sizeDefault));
    }

    public IngredientList getGroceryList() {
        ArrayList<String> grocery_list = mMenuList.GroceryList();
        return new IngredientList(grocery_list);
    }
    
    protected void LoadCookbook() {
        mCookbookList = new RecipeList();
        try{
            mCookbookList.ReadRecipeFile(this);
        }
        catch (RecipeList.RecipeListException e)
        {
            e.printStackTrace();
        }
    }
}