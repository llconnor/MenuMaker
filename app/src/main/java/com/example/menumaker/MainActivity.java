package com.example.menumaker;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.menumaker.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private RecipeList mCookbookList;
    private RecipeList mMenuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        // TODO: Figure out how to build the lists in the main and pass them down to Fragments
        LoadCookbook();
        Bundle bundle = new Bundle();
        bundle.putString("message", "Hello from llconnor");
        MainFragment frag_obj = new MainFragment();
        frag_obj.setArguments(bundle);
        // *** TODO Catch this on the other side

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
//        if (mMenuList.Length() == 0) {
            // TODO Remove hardcoding and replace with settings
            mMenuList = mCookbookList.makeMenu(5);
//        }
        return mMenuList;
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