package com.example.werescue;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

public class PetsFragment extends Fragment {

   @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_pets, container, false);

    RecyclerView recyclerView = view.findViewById(R.id.petsRecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    PetDatabaseHelper dbHelper = new PetDatabaseHelper(getActivity());
    SQLiteDatabase db = dbHelper.getReadableDatabase();

    String[] projection = {
            "id",
            "name",
            "description",
            "gender",
            "species",
            "birthday",
            "location",
            "weight",
            "imagePath"
    };

    Cursor cursor = db.query(
            "Pets",
            projection,
            null,
            null,
            null,
            null,
            null
    );

    List<DataClass> petList = new ArrayList<>();
    while (cursor.moveToNext()) {
        String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
        String species = cursor.getString(cursor.getColumnIndexOrThrow("species"));
        String birthday = cursor.getString(cursor.getColumnIndexOrThrow("birthday"));
        String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
        String weight = cursor.getString(cursor.getColumnIndexOrThrow("weight"));
        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath"));

        petList.add(new DataClass(id, name, description, gender, species, birthday, location, weight, imagePath));
    }
    cursor.close();

    PetAdapter adapter = new PetAdapter(getActivity(), petList);
    recyclerView.setAdapter(adapter);

    return view;
}
}