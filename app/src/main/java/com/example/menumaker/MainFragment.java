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
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

        //return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Figure out how to make the menu reappear when the activity is recreated.  Maybe this becomes another fragment...

        // Menu button
        binding.buttonMakeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the following lines to create RecyclerView
                // Got this from https://medium.com/swlh/create-recyclerview-in-android-fragment-c0f0b151125f
                // Also https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
                mRecyclerView = view.findViewById(R.id.recyclerview_menu);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                MakeMenu(v);
                mRecyclerView.setAdapter(new RecipeListAdapter(mMenuList));
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
                if (mMenuList == null) {
                    // TODO Make this a popup to ask the user to create a menu
                    MakeMenu(view);
                }
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_FirstFragment_to_ItemFragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = this.getView();
        assert view != null;
        mRecyclerView = view.findViewById(R.id.recyclerview_menu);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        /*if (mSavedInstanceState != null) {
            if (mSavedInstanceState.containsKey(getResources().getString(R.string.MenuKey))) {
                mMenuList = mSavedInstanceState.getParcelable(getResources().getString(R.string.MenuKey));
            }
        }
        else {
            // Figure out how to save this
            MakeMenu(view);
        }*/
        MakeMenu(view);
        mRecyclerView.setAdapter(new RecipeListAdapter(mMenuList));
    }

    protected void MakeMenu(View view) {
        MainActivity activityPtr = (MainActivity) getActivity();
        assert activityPtr != null;
        mMenuList = activityPtr.getMenuList();
    }

    public void saveMenu() {
        // TODO: Save the menuList
        /*mSavedInstanceState.putParcelable(getResources().getString(R.string.MenuKey), mMenuList);
        String filename = getResources().getString(R.string.menu_outfile);
        mMenuList.WriteToFile(filename, this.getContext());*/
    }

    @Override
    public void onPause() {
        super.onPause();
        saveMenu();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}