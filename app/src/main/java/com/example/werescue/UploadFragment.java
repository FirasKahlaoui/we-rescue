package com.example.werescue;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UploadFragment extends Fragment {

    private AppCompatButton uploadButton;
    private ImageView uploadImage;
    EditText petName, petDescription, speciesET, birthdayET, locationET, weightET;
    private Uri imageUri;
    final  private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        uploadImage = view.findViewById(R.id.uploadImage);
        uploadButton = view.findViewById(R.id.uploadButton);
        petName = view.findViewById(R.id.nameET);
        petDescription = view.findViewById(R.id.descriptionET);
        RadioButton maleRadioButton = view.findViewById(R.id.maleRadioButton);
        RadioButton femaleRadioButton = view.findViewById(R.id.femaleRadioButton);

        speciesET = view.findViewById(R.id.speciesET);
        birthdayET = view.findViewById(R.id.birthdayET);
        locationET = view.findViewById(R.id.locationET);
        weightET = view.findViewById(R.id.weightET);

        maleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maleRadioButton.isChecked()) {
                    femaleRadioButton.setChecked(false);
                }
            }
        });

        femaleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (femaleRadioButton.isChecked()) {
                    maleRadioButton.setChecked(false);
                }
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            imageUri = data.getData();
                            uploadImage.setImageURI(imageUri);
                        } else {
                            Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null){
                    uploadToFirebase(imageUri);
                } else  {
                    Toast.makeText(getActivity(), "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private String getGender() {
    RadioButton maleRadioButton = getView().findViewById(R.id.maleRadioButton);
    RadioButton femaleRadioButton = getView().findViewById(R.id.femaleRadioButton);

    if (maleRadioButton.isChecked()) {
        return "M";
    } else if (femaleRadioButton.isChecked()) {
        return "F";
    } else {
        return null;
    }
}
private void uploadToFirebase(Uri uri){
    // Get the values from the EditText fields
    String name = petName.getText().toString().trim();
    String description = petDescription.getText().toString().trim();
    String gender = getGender();
    String species = speciesET.getText().toString().trim();
    String birthday = birthdayET.getText().toString().trim();
    String location = locationET.getText().toString().trim();
    String weight = weightET.getText().toString().trim();

    // Check if any field is empty
    if(name.isEmpty() || description.isEmpty() || gender == null || species.isEmpty() || birthday.isEmpty() || location.isEmpty() || weight.isEmpty()) {
        Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        return;
    }

    String path = System.currentTimeMillis() + "." + getFileExtension(uri);
    final StorageReference imageReference = storageReference.child(path);
    Log.d("Upload Path", "Image will be uploaded to: " + path);

    imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Add the additional fields to the DataClass object
                    DataClass dataClass = new DataClass(uri.toString(), name, description, gender, species, birthday, location, weight);
                    String key = databaseReference.push().getKey();
                    databaseReference.child(key).setValue(dataClass)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Database Error", e.getMessage(), e);
                                }
                            });
                    Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                    ((MainActivity)getActivity()).replaceFragment(new HomeFragment());

                    // Convert the image to a byte array
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    if (bitmap != null) {
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
    byte[] imageBytes = outputStream.toByteArray();

    // Insert the pet into the local SQLite database
    PetDatabaseHelper dbHelper = new PetDatabaseHelper(getActivity());
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put("id", key);
    values.put("name", name);
    values.put("description", description);
    values.put("gender", gender);
    values.put("species", species);
    values.put("birthday", birthday);
    values.put("location", location);
    values.put("weight", weight);
    values.put("image", imageBytes);

    long newRowId = db.insert("Pets", null, values);
    if (newRowId == -1) {
        Log.e("SQLite Error", "Failed to insert row");
    } else {
        Log.d("SQLite", "Pet inserted with row id: " + newRowId);
    }
} else {
    // Handle the case where the bitmap is null
    Toast.makeText(getActivity(), "Failed to create bitmap from image", Toast.LENGTH_SHORT).show();
}
                }
            });
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.e("Upload Error", e.getMessage(), e);
            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
        }
    });
}
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}