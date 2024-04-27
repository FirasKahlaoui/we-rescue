package com.example.werescue;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DescriptionOwnerFragment extends Fragment {

    private ImageView backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description_owner, container, false);

        Bundle args = getArguments();
        DataClass pet = null;
        if (args != null) {
            pet = (DataClass) args.getSerializable("selectedPet");
        }

        backButton = view.findViewById(R.id.back_button);

        TextView petName = view.findViewById(R.id.petName);
        TextView petDescription = view.findViewById(R.id.petDescription);
        TextView petGender = view.findViewById(R.id.petGender);
        TextView petAge = view.findViewById(R.id.petAge);
        TextView petLocation = view.findViewById(R.id.petLocation);
        TextView petWeight = view.findViewById(R.id.petWeight);
        TextView ownerEmail = view.findViewById(R.id.owner_email);
        TextView petSpecies = view.findViewById(R.id.petSpecies);

        if (pet != null) {
            petName.setText(pet.getPetName());
            petDescription.setText(pet.getDescription());
            String gender = pet.getGender();
            if ("F".equals(gender)) {
                petGender.setText("Female");
            } else if ("M".equals(gender)) {
                petGender.setText("Male");
            }
            // Parse the birthday string into a LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birthday = LocalDate.parse(pet.getBirthday(), formatter);

            // Calculate the age
            Period period = Period.between(birthday, LocalDate.now());
            int age = period.getYears();

            // Display the age
            petAge.setText(age + " years");
            petLocation.setText(pet.getLocation());
            // Display the weight
            petWeight.setText(pet.getWeight() + " Kg");
            petSpecies.setText(pet.getSpecies());
            // Get the owner's email from shared preferences
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            String OwnerEmail = sharedPreferences.getString("email", "");
            ownerEmail.setText(OwnerEmail);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetsFragment petsFragment = new PetsFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, petsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}