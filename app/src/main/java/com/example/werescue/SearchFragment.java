package com.example.werescue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    private SearchFilter searchFilter;

    private Button buttonSearch;
    private TextView petName;
    private TextView petAge;
    private RadioButton petGenderMale;
    private RadioButton petGenderFemale;

    public interface OnDataSelectedListener {
        void onDataSelected(List<DataClass> pets);
    }

    private OnDataSelectedListener onDataSelectedListener;

    public void setOnDataSelectedListener(OnDataSelectedListener listener) {
        this.onDataSelectedListener = listener;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize the SearchFilter fragment
        searchFilter = (SearchFilter) getFragmentManager().findFragmentById(R.id.filterRecyclerView);

        initializeViews(view);
        setupSearchButton();

        return view;
    }

    private void initializeViews(View view) {
        petName = view.findViewById(R.id.nameET);
        petAge = view.findViewById(R.id.ageET);
        petGenderMale = view.findViewById(R.id.maleRadioButton);
        petGenderFemale = view.findViewById(R.id.femaleRadioButton);
        buttonSearch = view.findViewById(R.id.button_search);
    }

    private void setupSearchButton() {
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    private void performSearch() {
    String name = petName.getText().toString().trim();
    String age = petAge.getText().toString().trim();
    if (!petGenderMale.isChecked() && !petGenderFemale.isChecked()) {
        Toast.makeText(getContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
        return;
    }
    String gender = petGenderMale.isChecked() ? "Male" : "Female";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("pets");
    Query query = createQuery(databaseReference, name, age, gender);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DataClass> pets = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataClass data = snapshot.getValue(DataClass.class);
                    pets.add(data);
                }

                // Get a reference to the SearchFilter fragment
                SearchFilter searchFilter = (SearchFilter) getFragmentManager().findFragmentById(R.id.filterRecyclerView);

                // If the SearchFilter fragment is null, create a new instance
                if (searchFilter == null) {
                    searchFilter = new SearchFilter();
                }

                // Update the RecyclerView in the SearchFilter fragment
                searchFilter.updateData(pets);

                // Replace the current fragment with the SearchFilter fragment
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, searchFilter)
                        .commit();
            }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // Handle possible errors.
        }
    });
}
    private Query createQuery(DatabaseReference databaseReference, String name, String age, String gender) {
        Query query;

        if (!name.isEmpty() && !age.isEmpty()) {
            query = databaseReference.orderByChild("name").equalTo(name)
                    .orderByChild("age").equalTo(age)
                    .orderByChild("gender").equalTo(gender);
        } else if (!name.isEmpty()) {
            query = databaseReference.orderByChild("name").equalTo(name)
                    .orderByChild("gender").equalTo(gender);
        } else if (!age.isEmpty()) {
            query = databaseReference.orderByChild("age").equalTo(age)
                    .orderByChild("gender").equalTo(gender);
        } else {
            query = databaseReference.orderByChild("gender").equalTo(gender);
        }

        return query;
    }
}