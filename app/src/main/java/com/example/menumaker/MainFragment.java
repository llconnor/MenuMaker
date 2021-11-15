package com.example.menumaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menumaker.databinding.FragmentFirstBinding;

public class MainFragment extends Fragment {

    private FragmentFirstBinding binding;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Menu button
        binding.buttonMakeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the following lines to create RecyclerView
                // Got this from https://medium.com/swlh/create-recyclerview-in-android-fragment-c0f0b151125f
                // Also https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
                // TODO: Figure out how to make the RecyclerView not overlap with the buttons
                mRecyclerView = view.findViewById(R.id.recyclerview_menu);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                MakeMenu();
                mRecyclerView.setAdapter(new RecipeListAdapter(getMainMenu()));

            }
        });

        // View Cookbook button
        binding.buttonViewRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_FirstFragment_to_CookBookFragment);
            }
        });

        // View Grocery List button
        binding.buttonGrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getMainMenu() == null) {
                    Toast toast=Toast.makeText(MainFragment.super.getContext(),
                            "No Menu found.  Please click \"Make Menu\"",Toast.LENGTH_SHORT);
                    toast.setMargin(50,50);
                    toast.show();
                }
                else {
                    NavHostFragment.findNavController(MainFragment.this)
                            .navigate(R.id.action_FirstFragment_to_ItemFragment);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = this.getView();
        assert view != null;
        if (getMainMenu() != null) {
            mRecyclerView = view.findViewById(R.id.recyclerview_menu);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            mRecyclerView.setAdapter(new RecipeListAdapter(getMainMenu()));
        }
    }

    protected void MakeMenu() {
        MainActivity activityPtr = (MainActivity) getActivity();
        assert activityPtr != null;
        activityPtr.buildMenuList();
    }

    public void saveMenu() {
        String filename = getResources().getString(R.string.menu_outfile);
        getMainMenu().WriteToFile(filename, this.getContext());
    }

    public void loadMenu(){
        MainActivity activityPtr = (MainActivity) getActivity();
        assert activityPtr != null;
        activityPtr.loadMenu();
    }

    private RecipeList getMainMenu() {
        MainActivity activityPtr = (MainActivity) getActivity();
        assert activityPtr != null;
        return activityPtr.getMenuList();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveMenu();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveMenu();
        binding = null;
    }


}