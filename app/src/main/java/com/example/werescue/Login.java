package com.example.werescue;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Random;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends Activity {
    private boolean passwordShowing = false;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG ="Login";
    private FirebaseAuth mAuth;

    // Add this line to create a SharedPreferences object
    private SharedPreferences sharedPreferences;

    public String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+<>?{}[]";
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize the SharedPreferences object
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        // Check if the user is already logged in
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        final EditText emailET = (EditText) findViewById(R.id.emailET);
        final EditText passwordET = (EditText) findViewById(R.id.passwordET);
        final Button loginButton = (Button) findViewById(R.id.btn_login);
        final ImageView passwordIcon = findViewById(R.id.pass_icon);
        final TextView signUpBtn = findViewById(R.id.btn_register);
        final RelativeLayout googleLogin = findViewById(R.id.btn_google);
        final TextView btnForgetPass = findViewById(R.id.btn_forgot_pass);

        btnForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ForgetPass activity
                Intent intent = new Intent(Login.this, ResetPassCode.class);
                startActivity(intent);
            }
        });

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing) {
                    // If the password is currently showing, hide it and change the icon to show_pass
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.show_pass);
                    passwordShowing = false;
                } else {
                    // If the password is currently hidden, show it and change the icon to not_show_pass
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.not_show_pass);
                    passwordShowing = true;
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Register activity
                Intent intent = new Intent(Login.this, Sign_up.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Please enter your email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Get the user's name and email
                            String name = user.getDisplayName();
                            String email = user.getEmail();

                            // If the name is null, use the first part of the email as the name
                            if (name == null && email != null) {
                                name = email.split("@")[0];
                            }

                            // Save the name and email in shared preferences
                            SharedPreferences sharedPreferences = Login.this.getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.apply();

                            SharedPreferences.Editor loginEditor = sharedPreferences.edit();
                            loginEditor.putBoolean("isLoggedIn", true);
                            loginEditor.apply();

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
});
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestIdToken(getString(R.string.default_web_client_id)) // Add this line
    .requestEmail()
    .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

googleLogin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // Sign out the user
        mGoogleSignInClient.signOut();

        // Start the sign-in process
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
});
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
        GoogleSignInAccount account = completedTask.getResult(ApiException.class);

        // Signed in successfully, authenticate with Firebase
        firebaseAuthWithGoogle(account.getIdToken(), account.getEmail());
    } catch (ApiException e) {
        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        updateUI(null);
    }
}

    private void firebaseAuthWithGoogle(String idToken, String email) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "Logged in", Toast.LENGTH_SHORT).show();

                            // Get the user's name and email
                            String name = user.getDisplayName();
                            String email = user.getEmail();

                            // If the name is null, use the first part of the email as the name
                            if (name == null && email != null) {
                                name = email.split("@")[0];
                            }

                            // Save the name and email in shared preferences
                            SharedPreferences sharedPreferences = Login.this.getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.apply();

                            // Save the login state in shared preferences
                            SharedPreferences.Editor loginEditor = sharedPreferences.edit();
                            loginEditor.putBoolean("isLoggedIn", true);
                            loginEditor.apply();

                            updateUI(user);
                        } else {
                            // If sign in fails, log the exception
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                    // Check if the email already exists
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        // User already exists, navigate to MainActivity
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Login.this, "Logged in", Toast.LENGTH_SHORT).show();
                        updateUI(user);
                    } else {
                        // Generate a random password
                        String password = generateRandomPassword(10);

                        // Create a new account
                        mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign up success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(Login.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                        updateUI(user);
                                    } else {
                                        // If sign up fails, log the exception and display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                    }
                }
            }
        });
}

private void updateUI(FirebaseUser user) {
    if (user != null) {
        // User is signed in
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    } else {
        // User is not signed in
        Toast.makeText(Login.this, "Sign is failed", Toast.LENGTH_SHORT).show();
    }
}
}