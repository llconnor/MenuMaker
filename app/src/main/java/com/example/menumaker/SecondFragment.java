package com.example.menumaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menumaker.databinding.FragmentSecondBinding;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private RecipeList mCookbook;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);

        // *** TODO beginning
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        // Add the following lines to create RecyclerView
        // Got this from https://medium.com/swlh/create-recyclerview-in-android-fragment-c0f0b151125f
        // Also https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new RandomNumListAdapter(1234));

        return view;
        // *** TODO end
        //return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //DisplayCookBook();
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected void DisplayCookBook() {
        String recipes = "  ";
        MainActivity activityptr = (MainActivity) getActivity();
        mCookbook = activityptr.getCookBookList();
        // TODO Change this to a "listView of the recipe names
        for (int i = 0; i < mCookbook.Length(); i++) {
            recipes += mCookbook.getRecipeAtPosition(i).getRecipeName() + "\n";
        }
        //binding.textviewSecond.setText(recipes);
    }
}