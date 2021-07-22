package com.example.menumaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menumaker.databinding.FragmentFirstBinding;

public class MainFragment extends Fragment {

    private FragmentFirstBinding binding;
    private RecyclerView mRecyclerView;
    private RecipeList mMenuList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        //DisplayMenu(view);
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

        //return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Figure out how to make the menu reappear when the activity is recreated.  Maybe this becomes another fragment...

        binding.buttonMakeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Remove comment when we get the settings fragment working
                //NavHostFragment.findNavController(FirstFragment.this)
                //        .navigate(R.id.action_FirstFragment_to_SettingFragment);
                // Add the following lines to create RecyclerView
                // Got this from https://medium.com/swlh/create-recyclerview-in-android-fragment-c0f0b151125f
                // Also https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
                MainActivity activityptr = (MainActivity) getActivity();
                mRecyclerView = view.findViewById(R.id.recyclerview_menu);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                mMenuList = activityptr.getMenuList();
                mRecyclerView.setAdapter(new RecipeListAdapter(mMenuList));
            }
        });

        binding.buttonViewRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_FirstFragment_to_CookBookFragment);
            }
        });
        binding.buttonGrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_FirstFragment_to_ItemFragment);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}