package com.example.teja.calorie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WorkoutActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText workoutType, workoutCount;
    Button addWorkout;

    private final String _type = "workout";
    CalorieCalculator calorieCalculator = new CalorieCalculator(_type);
    CalorieModel calorieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        workoutType = (EditText) findViewById(R.id.workout_type);
        workoutCount = (EditText) findViewById(R.id.workout_count);
        addWorkout = (Button) findViewById(R.id.btn_addWorkout);

        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWorkout(workoutType.getText().toString().toLowerCase(), Double.parseDouble(workoutCount.getText().toString()));
                Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_LONG).show();
                workoutType.setText("");
                workoutCount.setText("");
            }
        });
    }

    public void addWorkout(String workoutType, Double workoutCount){
        String uid = getUid();
        double calories = calorieCalculator.getCalories(workoutType, workoutCount);
        calorieModel = new CalorieModel(uid, _type, workoutType, "" + workoutCount + " hours", calories);
        mDatabase.child("workout").push().setValue(calorieModel);
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
