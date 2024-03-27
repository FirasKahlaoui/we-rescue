package com.example.werescue;

import static com.example.werescue.R.*;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends Activity {
    private boolean passwordShowing = false;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // Your code here

        mAuth = FirebaseAuth.getInstance();
        final EditText emailET = (EditText) findViewById(R.id.emailET);
        final EditText passwordET = (EditText) findViewById(R.id.passwordET);
        final Button loginButton = (Button) findViewById(R.id.btn_login);
        final ImageView passwordIcon = findViewById(R.id.pass_icon);
        final TextView signUpBtn = findViewById(R.id.btn_register);
        final TextView googleLogin = findViewById(R.id.btn_google);


        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            //Checking the password is showing or not
            public void onClick(View v) {
                if (passwordShowing){
                    passwordShowing = false;
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.show_pass);
                }else {
                    passwordShowing = true;
                    passwordET.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.not_show_pass);
                }
                //move the cursor to the end of the text
                passwordET.setSelection(passwordET.length());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Sign_up.class);
                startActivity(intent);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                if (email.isEmpty()){
                    Toast.makeText(Login.this, "Email field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.matches("^[\\w.-]+@(gmail|outlook|yahoo|icloud)\\.com$")){
                    Toast.makeText(Login.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()){
                    Toast.makeText(Login.this, "Password field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }



                //sign in the user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}