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

public class Login extends Activity {
    private boolean passwordShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // Your code here

        final EditText emailET = (EditText) findViewById(R.id.emailET);
        final EditText passwordET = (EditText) findViewById(R.id.passwordET);
        final Button loginButton = (Button) findViewById(R.id.btn_login);
        final ImageView passwordIcon = findViewById(R.id.pass_icon);
        final TextView signUpBtn = findViewById(R.id.btn_register);


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


        /*loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                if (email.equals("admin") && password.equals("admin")) {
                    //Intent intent = new Intent(Login.this, Home.class);
                    //startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }
}