package com.example.teja.calorie;

import java.util.HashMap;

/**
 * Created by teja on 4/27/17.
 */

public class CalorieCalculator {

    private HashMap<String, Integer> foodMap = new HashMap<>();
    private HashMap<String, Integer> workoutMap = new HashMap<>();

    String _type = null;

    CalorieCalculator(String _type){
        this._type = _type;
    }

    void getFoodMap() {
        foodMap.put("potato", 283);
        foodMap.put("milk", 103);
        foodMap.put("banana", 105);
        foodMap.put("bread", 79);
        foodMap.put("kidney beans", 613);
        foodMap.put("rice", 206);
        foodMap.put("avacado", 234);
        foodMap.put("oat meal", 158);
        foodMap.put("wheat", 651);
        foodMap.put("eggs", 78);
        foodMap.put("chicken", 335);
        foodMap.put("beef", 213);
        foodMap.put("cereal", 307);
        //add in more items here
    }

    void getWorkoutMap() {
        workoutMap.put("swimming", -500);
        workoutMap.put("running", -500);
        workoutMap.put("walking", -170);
        workoutMap.put("cycling", -472);
        workoutMap.put("dancing", -325);
        workoutMap.put("badminton", -266);
        workoutMap.put("aerobics", -384);
        //add in more items here
    }

    public Double getCalories(String type, double count){
        if(_type.equals("workout")){
            if(workoutMap.containsKey(type)){
                return -(workoutMap.get(type) * count);
            }else{
                return -150 * count;
            }
        }else{
            if(foodMap.containsKey(type)){
                return foodMap.get(type) * count;
            }else{
                return 150 * count;
            }
        }
    }
}