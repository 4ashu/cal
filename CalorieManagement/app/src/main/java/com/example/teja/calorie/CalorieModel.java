package com.example.teja.calorie;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by teja on 4/27/17.
 */
@IgnoreExtraProperties
public class CalorieModel {
    String user, _type, name, count;
    double calories;

    public CalorieModel(){
        //default constructor
    }

    public CalorieModel(String user, String _type, String name, String count, double calories) {
        this.user = user;
        this._type = _type;
        this.name = name;
        this.count = count;
        this.calories = calories;
    }

    public String getUser() {
        return user;
    }

    public String get_type() {
        return _type;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }

    public double getCalories() {
        return calories;
    }

    @Override
    public String toString() {
        return "CalorieModel{" +
                "user='" + user + '\'' +
                ", _type='" + _type + '\'' +
                ", name='" + name + '\'' +
                ", count='" + count + '\'' +
                ", calories=" + calories +
                '}';
    }
}
