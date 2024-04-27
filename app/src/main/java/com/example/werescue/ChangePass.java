package com.example.werescue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePass extends AppCompatActivity {

    Button btnBack, btnChangePassword;
    EditText newPasswordInput;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        btnBack = findViewById(R.id.button_back);
        btnChangePassword = findViewById(R.id.submitButton); // replace with your actual button id
        newPasswordInput = findViewById(R.id.new_passwordET); // replace with your actual EditText id

        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePass.this, ProfileFragment.class);
                startActivity(intent);
                finish();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    String newPassword = newPasswordInput.getText().toString();
                    user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangePass.this, "Password updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ChangePass.this, "Password update failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });
    }
}