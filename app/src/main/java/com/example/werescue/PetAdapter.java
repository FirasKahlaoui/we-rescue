package com.example.werescue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {
    private Context context;
    private List<DataClass> petList;

    public PetAdapter(Context context, List<DataClass> petList) {
        this.context = context;
        this.petList = petList;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new PetViewHolder(view);
    }

@Override
public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
    DataClass pet = petList.get(position);
    holder.petName.setText(pet.getPetName());
    holder.petLocation.setText(pet.getLocation());

    // Get the file path
    String filePath = pet.getImagePath();

    // Use Glide to load the image from the file path
    Glide.with(context)
        .load(filePath)
        .into(holder.petImage);
}

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder {
        ImageView petImage;
        TextView petName;
        TextView petLocation;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            petImage = itemView.findViewById(R.id.recyclerImage);
            petName = itemView.findViewById(R.id.recyclerCaptionName);
            petLocation = itemView.findViewById(R.id.recyclerCaptionLocation);
        }
    }
}