package com.example.werescue;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_filter, container, false);

        recyclerView = view.findViewById(R.id.filterRecyclerView);

        return view;
    }

    // Method to create a new RecyclerView with the new data
    // Method to create a new RecyclerView with the new data
    public void updateData(List<DataClass> pets) {
        if (recyclerView == null) {
            View view = getView();
            if (view != null) {
                recyclerView = view.findViewById(R.id.petsRecyclerView);
            }
        }
        if (recyclerView != null) {
            MyAdapter myAdapter = new MyAdapter(pets);
            recyclerView.setAdapter(myAdapter);
        }
    }
}