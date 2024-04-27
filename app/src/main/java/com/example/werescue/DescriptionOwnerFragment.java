package com.example.werescue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        TextView petBirthday = view.findViewById(R.id.petAge);
        TextView petLocation = view.findViewById(R.id.petLocation);
        TextView petWeight = view.findViewById(R.id.petWeight);
        TextView petSpecies = view.findViewById(R.id.petSpecies);

        if (pet != null) {
            petName.setText(pet.getPetName());
            petDescription.setText(pet.getDescription());
            petGender.setText(pet.getGender());
            petBirthday.setText(pet.getBirthday());
            petLocation.setText(pet.getLocation());
            petWeight.setText(pet.getWeight());
            petSpecies.setText(pet.getSpecies());
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