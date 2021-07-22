package com.example.menumaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    //private Random random;
    IngredientList mIngredientList;

    public IngredientListAdapter(IngredientList ingredientList) {
        this.mIngredientList = ingredientList;
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
        holder.getView().setText(String.valueOf(mIngredientList.getIngredientAtPosition(position)));
    }

    @Override
    public int getItemCount() {
        return mIngredientList.getIngredientList().size();
    }


}
