package com.example.teja.calorie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signUpLink;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //UI elements
        loginEmail = (EditText) findViewById(R.id.input_email);
        loginPassword = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        signUpLink = (TextView) findViewById(R.id.link_signup);

        //if previously logged in

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(i);
                    // User is signed in
                    // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    // Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }

        };


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login(){
        loginButton.setEnabled(false);
        progressDialog = ProgressDialog.show(LoginActivity.this,"Authenticating", "Please wait...", true);

        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String message = "";
                        if(task.isSuccessful()){
                            message = "You are now logged in";

                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            // On complete call either onLoginSuccess or onLoginFailed
                                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                            startActivity(intent);
                                            onLoginSuccess();
                                            // onLoginFailed();
                                            progressDialog.dismiss();
                                        }
                                    }, 2000);
                        }
                        if (!task.isSuccessful()) {
                            message = "Please try again";
                            progressDialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void onLoginSuccess(){
        loginButton.setEnabled(true);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
