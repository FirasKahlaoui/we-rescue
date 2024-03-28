package com.example.werescue;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Search extends Fragment {

    private Button buttonSearch;
    private String category = "All";
    private String gender = "";
    private String age = "";
    private String size = "";

    private DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonSearch = view.findViewById(R.id.button_search);

        setupButtonGroup(view, R.id.grid_layout_category, selected -> category = selected);
        setupButtonGroup(view, R.id.linear_layout_gender, selected -> gender = selected);
        setupButtonGroup(view, R.id.linear_layout_age, selected -> age = selected);
        setupButtonGroup(view, R.id.linear_layout_size, selected -> size = selected);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = mDatabase.child("pets");

                if (!category.equals("All")) {
                    query = query.orderByChild("category").equalTo(category);
                }

                if (!gender.isEmpty()) {
                    query = query.orderByChild("gender").equalTo(gender);
                }

                if (!age.isEmpty()) {
                    query = query.orderByChild("age").equalTo(age);
                }

                if (!size.isEmpty()) {
                    query = query.orderByChild("size").equalTo(size);
                }

                // Execute the query and handle the results
                // ...
            }
        });

        return view;
    }

    private void setupButtonGroup(View view, int groupId, OnButtonSelectedListener listener) {
    ViewGroup buttonGroup = view.findViewById(groupId);
    for (int i = 0; i < buttonGroup.getChildCount(); i++) {
        Button button = (Button) buttonGroup.getChildAt(i);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset the background color of all buttons
                for (int j = 0; j < buttonGroup.getChildCount(); j++) {
                    Button otherButton = (Button) buttonGroup.getChildAt(j);
                    otherButton.setBackgroundColor(Color.GRAY);
                }

                // Change the background color of the selected button
                button.setBackgroundColor(Color.GREEN);

                // Notify the listener about the selected button
                listener.onButtonSelected(button.getText().toString());
            }
        });
    }
}

    interface OnButtonSelectedListener {
        void onButtonSelected(String selected);
    }
}