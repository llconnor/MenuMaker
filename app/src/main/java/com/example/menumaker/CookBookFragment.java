package com.example.menumaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menumaker.databinding.FragmentSecondBinding;

public class CookBookFragment extends Fragment {

    private FragmentSecondBinding binding;
    private RecipeList mCookbook;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        DisplayCookBook(view);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected void DisplayCookBook(View view) {
        String recipes = "  ";
        MainActivity activityptr = (MainActivity) getActivity();
        mCookbook = activityptr.getCookBookList();
        // TODO Change this to a "listView of the recipe names
        for (int i = 0; i < mCookbook.Length(); i++) {
            recipes += mCookbook.getRecipeAtPosition(i).getRecipeName() + "\n";
        }
        // Add the following lines to create RecyclerView
        // Got this from https://medium.com/swlh/create-recyclerview-in-android-fragment-c0f0b151125f
        // Also https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new RecipeListAdapter(mCookbook));
    }
}