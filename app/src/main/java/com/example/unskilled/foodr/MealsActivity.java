package com.example.unskilled.foodr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.unskilled.foodr.API.DatabaseCommands;
import com.example.unskilled.foodr.Adapters.MealAdapter;
import com.example.unskilled.foodr.SQLite.DBHandler;
import com.example.unskilled.foodr.classes.Meal;

import java.util.ArrayList;
import java.util.Collections;

public class MealsActivity extends AppCompatActivity {

    private ListView mealsListView = null;
    private MealAdapter melasAdapter = null;
    private FloatingActionButton addButton;

    private ArrayList<Meal> meals = new ArrayList<Meal>();

    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        db = new DBHandler(getApplicationContext(), null, null, 1);

        DatabaseCommands.updateAllMeals(db);
        DatabaseCommands.updateAllUsers(db);

        meals = db.getAllMeals();
        Collections.reverse(meals);

        mealsListView = (ListView) findViewById(R.id.meals_listview);
        addButton = (FloatingActionButton) findViewById(R.id.btn_meals_add);

        melasAdapter = new MealAdapter(getApplicationContext(), getLayoutInflater(), meals, db);
        mealsListView.setAdapter(melasAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MealsActivity.this, NewMealActivity.class);
                MealsActivity.this.startActivity(myIntent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        DatabaseCommands.updateAllMeals(db);
        meals = db.getAllMeals();
        melasAdapter.notifyDataSetChanged();
        super.onResume();
    }

}
