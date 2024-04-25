package com.example.werescue;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class PetDescriptionFragment extends Fragment {

    private ImageView petImage;
    private TextView petGender;
    private TextView petWeight;
    private TextView petAge;
    private TextView petDescription;
    private TextView petName;

    private ImageView backButton;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_description, container, false);

        // Initialize the views
        petImage = view.findViewById(R.id.petImage);
        petGender = view.findViewById(R.id.petGender);
        petWeight = view.findViewById(R.id.petWeight);
        petAge = view.findViewById(R.id.petAge);
        petDescription = view.findViewById(R.id.petDescription);
        petName = view.findViewById(R.id.petName);
        backButton = view.findViewById(R.id.back_button);

        // Get the pet data from the Bundle
        DataClass petData = (DataClass) getArguments().getSerializable("petData");

        // Check if petData is not null before using it
        if (petData != null) {
            // Use the pet data to update the views
            Glide.with(this).load(petData.getImageURL()).into(petImage);
            petGender.setText(petData.getGender());
            petWeight.setText(String.valueOf(petData.getWeight()));
            petAge.setText(String.valueOf(petData.getBirthday()));
            petDescription.setText(petData.getDescription());
            petName.setText(petData.getPetName());
        }

        else {
            // If petData is null, display a message
            Toast.makeText(getContext(), "No pet data found", Toast.LENGTH_SHORT).show();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, homeFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}