package com.example.werescue;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.werescue.MyAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private TextView noMatchesTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_filter, container, false);

        recyclerView = view.findViewById(R.id.filterRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myAdapter = new MyAdapter(new ArrayList<>());
        recyclerView.setAdapter(myAdapter);

        noMatchesTextView = view.findViewById(R.id.noMatchesTextView);

        return view;
    }

    public void updateData(List<DataClass> pets) {
    if (myAdapter != null) {
        myAdapter.updateData(pets);
    }

    if (noMatchesTextView != null) {
        if (pets.isEmpty()) {
            noMatchesTextView.setVisibility(View.VISIBLE);
        } else {
            noMatchesTextView.setVisibility(View.GONE);
        }
    }
}
}