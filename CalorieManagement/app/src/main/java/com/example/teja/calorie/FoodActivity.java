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

public class FoodActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText foodType, foodCount;
    Button addFood;

    private final String _type = "food";
    CalorieCalculator calorieCalculator = new CalorieCalculator(_type);
    CalorieModel calorieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        foodType = (EditText) findViewById(R.id.food_type);
        foodCount = (EditText) findViewById(R.id.food_quantity);
        addFood = (Button) findViewById(R.id.btn_addFood);

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(foodType.getText().toString().toLowerCase(), Double.parseDouble(foodCount.getText().toString()));
                Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_LONG).show();
                foodType.setText("");
                foodCount.setText("");
            }
        });
    }

    public void addFood(String foodType, Double foodCount){
        String uid = getUid();
        double calories = calorieCalculator.getCalories(foodType, foodCount);
        calorieModel = new CalorieModel(uid, _type, foodType, "" + foodCount + " pieces", calories);
        mDatabase.child("calories").push().setValue(calorieModel);
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
