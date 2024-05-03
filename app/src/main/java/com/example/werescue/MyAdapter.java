package com.example.werescue;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<DataClass> dataList;
    private Context context;
    private DatabaseReference databaseReference;
    private List<DataClass> originalDataList;
    private List<DataClass> filteredDataList;
    private List<DataClass> pets;

    public MyAdapter(Context context, List<DataClass> pets) {
        this.context = context;
        this.dataList = pets != null ? pets : new ArrayList<>();
    }

    public MyAdapter(Context context, DatabaseReference databaseReference) {
        this.context = context;
        this.databaseReference = databaseReference;
        this.dataList = new ArrayList<>();
        this.originalDataList = new ArrayList<>();
        this.filteredDataList = new ArrayList<>();
        loadData();
    }

    public MyAdapter(List<DataClass> pets) {
        this.context = null;
        this.databaseReference = null;
        this.dataList = new ArrayList<>(pets);
        this.originalDataList = new ArrayList<>(pets);
        this.filteredDataList = new ArrayList<>(pets);
    }


    private void loadData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataClass data = snapshot.getValue(DataClass.class);
                    dataList.add(data);
                }
                originalDataList = new ArrayList<>(dataList);
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(dataList.get(position).getImageURL()).into(holder.recyclerImage);
        holder.recyclerCaption.setText(dataList.get(position).getPetName());
        holder.recyclerCaptionLocation.setText(dataList.get(position).getLocation());

        // Set an OnClickListener on the ViewHolder
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of PetDescriptionFragment
                PetDescriptionFragment petDescriptionFragment = new PetDescriptionFragment();

                // Create a Bundle to pass the pet data
                Bundle bundle = new Bundle();
                bundle.putSerializable("petData", (Serializable) dataList.get(position));
                petDescriptionFragment.setArguments(bundle);

                // Replace the current fragment with PetDescriptionFragment
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, petDescriptionFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public void updateData(List<DataClass> pets) {
        this.dataList = pets;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView recyclerImage;
        TextView recyclerCaption;
        TextView recyclerCaptionLocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerImage = itemView.findViewById(R.id.recyclerImage);
            recyclerCaption = itemView.findViewById(R.id.recyclerCaptionName);
            recyclerCaptionLocation = itemView.findViewById(R.id.recyclerCaptionLocation);
        }
    }

    public void filter(String text) {
        ArrayList<DataClass> filteredList = new ArrayList<>();
        for (DataClass item : dataList) {
            if (item.getPetName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        this.dataList = filteredList;
        notifyDataSetChanged();
    }

    public void showAllItems() {
        this.dataList = new ArrayList<>(this.originalDataList);
        notifyDataSetChanged();
    }
    public void setDataList(ArrayList<DataClass> dataList) {
        this.dataList = dataList;
    }

}
