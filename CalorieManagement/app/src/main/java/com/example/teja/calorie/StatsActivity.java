package com.example.teja.calorie;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    CalorieModel calorieModel;

    TextView intake, burn, netCalorie;
    Button reset;

    ProgressDialog progressDialog;

    double calorieIntake = 0, calorieBurnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        intake = (TextView) findViewById(R.id.calorie_intake);
        burn = (TextView) findViewById(R.id.calorie_burn);
        netCalorie = (TextView) findViewById(R.id.calorie_progress);
        reset = (Button) findViewById(R.id.resetAll);

        progressDialog = ProgressDialog.show(StatsActivity.this,"Analyzing", "Please wait...", true);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        getData();
                        progressDialog.dismiss();
                    }
                }, 3000);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData();
            }
        });
    }

    public void getData(){

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String uid = getUid();
                DataSnapshot calorieSnapShot = dataSnapshot.child("calories");
                for(DataSnapshot snapshot : calorieSnapShot.getChildren()){
                        calorieModel = snapshot.getValue(CalorieModel.class);
                        if(calorieModel.getUser().equals(uid)){
                            calorieIntake += calorieModel.getCalories();
                        }
                }

                DataSnapshot workOutSnapShot = dataSnapshot.child("workout");
                for(DataSnapshot snapshot : workOutSnapShot.getChildren()){
                        calorieModel = snapshot.getValue(CalorieModel.class);
                        if(calorieModel.getUser().equals(uid)){
                            calorieBurnt += calorieModel.getCalories();
                        }
                }

                intake.setText(intake.getText().toString() + ": "  + calorieIntake);
                burn.setText(burn.getText().toString() + ": " + calorieBurnt);
                netCalorie.setText(netCalorie.getText().toString() + ": " + Math.abs(calorieIntake - calorieBurnt));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DEMO", "Failed to read value.", error.toException());
            }
        });
    }

    public void removeData(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String uid = getUid();
                DataSnapshot calorieSnapShot = dataSnapshot.child("calories");
                for(DataSnapshot snapshot : calorieSnapShot.getChildren()){
                    calorieModel = snapshot.getValue(CalorieModel.class);
                    if(calorieModel.getUser().equals(uid)){
                        snapshot.getRef().removeValue();
                    }
                }

                DataSnapshot workOutSnapShot = dataSnapshot.child("workout");
                for(DataSnapshot snapshot : workOutSnapShot.getChildren()){
                    calorieModel = snapshot.getValue(CalorieModel.class);
                    if(calorieModel.getUser().equals(uid)){
                        snapshot.getRef().removeValue();
                    }
                }

                Toast.makeText(getApplicationContext(), "Successfully removed all the entries", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DEMO", "Failed to read value.", error.toException());
            }
        });
    }

    public String getUid(){
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // UID specific to the user
                return profile.getUid();
            };
        }
        return null;
    }
}
