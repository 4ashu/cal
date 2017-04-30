package com.example.teja.calorie;

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

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText signUpEmail, signUpPassword, signUpConfirmPassword;
    Button signUpButton;
    TextView loginLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        //UI elements
        signUpEmail = (EditText) findViewById(R.id.input_email);
        signUpPassword = (EditText) findViewById(R.id.input_password);
        signUpConfirmPassword = (EditText) findViewById(R.id.input_cpassword);
        signUpButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = signUpEmail.getText().toString();
                String password = signUpPassword.getText().toString();
                String confirmPassword = signUpConfirmPassword.getText().toString();
                if (password.equals(confirmPassword)) {
                    signUp(email, password);
                }else{
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                }
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void signUp(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            onSignUpSuccess();
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Please try again ..", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    public void onSignUpSuccess(){
        Toast.makeText(getApplicationContext(), "Thank you for Signing Up", Toast.LENGTH_SHORT).show();
        new android.os.Handler().postDelayed(
                new Runnable() { public void run() { finish(); } }, 1000);
    }

}
