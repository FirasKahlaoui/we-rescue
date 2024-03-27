package com.example.werescue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.werescue.R.id;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Sign_up extends Activity{

    private boolean passwordShowing = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        // Your code here

        mAuth = FirebaseAuth.getInstance();
        final EditText emailET = (EditText) findViewById(R.id.emailET);
        final EditText fullNameET = (EditText) findViewById(R.id.fullNameET);
        final EditText passwordET = (EditText) findViewById(R.id.passwordET);
        final Button signUpButton = (Button) findViewById(R.id.btn_register);
        final ImageView passwordIcon = findViewById(R.id.pass_icon);
        final EditText confirmPasswordET = (EditText) findViewById(R.id.conf_passwordET);
        final TextView loginButton = findViewById(R.id.btn_login);
        final TextView passwordErrorTV = findViewById(R.id.passwordErrorTV);

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing){
                    passwordShowing = false;
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.show_pass);
                }else {
                    passwordShowing = true;
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirmPasswordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.not_show_pass);
                }
                //move the cursor to the end of the text
                passwordET.setSelection(passwordET.length());
                confirmPasswordET.setSelection(confirmPasswordET.length());
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_up.this, Login.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                final String fullName = fullNameET.getText().toString();
                String password = passwordET.getText().toString();
                String confirmPassword = confirmPasswordET.getText().toString();

                if (email.isEmpty() && fullName.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()){
                    Toast.makeText(Sign_up.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.isEmpty()){
                    Toast.makeText(Sign_up.this, "Email field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.matches("^[\\w.-]+@(gmail|outlook|yahoo|icloud)\\.com$")){
                    Toast.makeText(Sign_up.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fullName.isEmpty()){
                    Toast.makeText(Sign_up.this, "Full Name field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()){
                    Toast.makeText(Sign_up.this, "Password field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6){
                    Toast.makeText(Sign_up.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
                    passwordErrorTV.setText("Password must contain at least one uppercase letter, one number and one symbol");
                    passwordErrorTV.setVisibility(View.VISIBLE);
                    return;
                } else {
                    passwordErrorTV.setVisibility(View.GONE);
                }
                if (confirmPassword.isEmpty()){
                    Toast.makeText(Sign_up.this, "Confirm Password field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)){
                    Toast.makeText(Sign_up.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                //sign up the user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Sign_up.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(fullName)
                                            .build();

                                    if (user != null) {
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Sign_up.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(Sign_up.this, Login.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(Sign_up.this, "Profile update failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }

                                    // Update UI with user information
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Sign_up.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
}
