package com.example.menumaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    //private Random random;
    RecipeList mRecipeList;

    public RecipeListAdapter(RecipeList recipeList) {
        this.mRecipeList = recipeList;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_textview;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        //holder.getView().setText(String.valueOf(random.nextInt()));
        holder.getView().setText(String.valueOf(mRecipeList.getRecipeAtPosition(position)));
    }

    @Override
    public int getItemCount() {
        return mRecipeList.getNumRecipes();
        //return 100;
    }


}
