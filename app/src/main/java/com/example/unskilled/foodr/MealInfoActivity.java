package com.example.unskilled.foodr;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.unskilled.foodr.API.APIRequest;
import com.example.unskilled.foodr.API.DatabaseCommands;
import com.example.unskilled.foodr.Helpers.SavedDataHelper;
import com.example.unskilled.foodr.SQLite.DBHandler;
import com.example.unskilled.foodr.classes.Eater;
import com.example.unskilled.foodr.classes.Meal;
import com.example.unskilled.foodr.classes.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MealInfoActivity extends AppCompatActivity {

    private static final String TAG = "MealInfoActivity";

    private TextView nameText, descriptionText, priceText, guestText, placeText, datetimeText, checkinText, cookText;
    private ImageView headerPhoto, checkinPhoto;
    private LinearLayout layoutGuests;
    private Button addEaterButton, deleteButton;

    private Meal currentMeal;
    private int meal_id;

    private DBHandler udb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_info);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        meal_id = getIntent().getIntExtra("MEAL_ID", 0);

        udb = new DBHandler(getApplicationContext(), null, null, 1);
        getEatersForMeal(meal_id);


        nameText = (TextView) findViewById(R.id.txt_meal_name);
        cookText = (TextView) findViewById(R.id.txt_meal_cook);
        descriptionText = (TextView) findViewById(R.id.txt_meal_description);
        priceText = (TextView) findViewById(R.id.txt_meal_price);
        guestText = (TextView) findViewById(R.id.txt_meal_total_guests);
        placeText = (TextView) findViewById(R.id.txt_meal_place);
        datetimeText = (TextView) findViewById(R.id.txt_meal_time);
        headerPhoto = (ImageView) findViewById(R.id.image_view_meal_info);
        checkinPhoto = (ImageView) findViewById(R.id.image_meal_info_checkin);
        checkinText = (TextView) findViewById(R.id.txt_meal_info_checkin);

        layoutGuests = (LinearLayout) findViewById(R.id.layout_meal_show_all_guests);

        addEaterButton = (Button) findViewById(R.id.button_meal_add_eater);
        deleteButton = (Button) findViewById(R.id.button_meal_delete);

        currentMeal = udb.getMealById(meal_id);

        User cook = udb.getUserById(currentMeal.getCook());

        nameText.setText(currentMeal.getName());
        cookText.setText(cook.getFirstName() + " " + cook.getLastName());
        descriptionText.setText(currentMeal.getDescription());
        priceText.setText(String.valueOf(currentMeal.getPrice()));
        guestText.setText(String.valueOf(currentMeal.getMaxGuests()));
        placeText.setText(currentMeal.getPlace());
        datetimeText.setText(currentMeal.getTime());

        Picasso.with(this).load(currentMeal.getImage())
                .fit()
                .centerCrop()
                .into(headerPhoto);

        addEaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealInfoActivity.this, MealInfoPopUp.class);
                intent.putExtra("MEAL_ID", meal_id);
                intent.putExtra("TOTAL_GUEST", currentMeal.getMaxGuests());
                MealInfoActivity.this.startActivity(intent);
            }
        });

        layoutGuests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealInfoActivity.this, EatersActivity.class);
                intent.putExtra("MEAL_ID", meal_id);
                MealInfoActivity.this.startActivity(intent);
            }
        });

        if (currentMeal.getCook() == SavedDataHelper.getCurrentUser().getId()) {
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseCommands.deleteMeal(currentMeal.getId());
                    Intent myIntent = new Intent(MealInfoActivity.this, MealsActivity.class);
                    MealInfoActivity.this.startActivity(myIntent);
                    finish();
                }
            });
        }

        layout();
    }

    private void layout(){

        int checkInCount = DatabaseCommands.getCountEaters(meal_id);
        boolean checked = checkInChecker(meal_id);

        if (checked) {
            addEaterButton.setVisibility(View.INVISIBLE);

            checkinText.setText("U staat ingeschreven voor deze maaltijd");
            Picasso.with(this).load(R.drawable.check_mark)
                    .fit()
                    .centerCrop()
                    .into(checkinPhoto);
        } else if (checkInCount >= currentMeal.getMaxGuests()) {
            addEaterButton.setVisibility(View.GONE);
            checkinText.setText("Deze maaltijd is vol geboekt");
        }
    }

    private boolean checkInChecker(int meal_id) {
        boolean checked = false;
        ArrayList<Eater> eaters = udb.getAllEatersByMealId(meal_id);
        for (Eater eater : eaters) {
            if (eater.getGuest() == SavedDataHelper.getCurrentUser().getId()) {
                checked = true;
            }
        }

        return checked;
    }

    private void getEatersForMeal(int id) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", SavedDataHelper.getToken());


        udb.deleteAllEaters();
        JSONArray eaters = (JSONArray) APIRequest.GET("getEaters/" + id, map);
        try {

            for (int idx = 0; idx < eaters.length(); idx++) {

                JSONObject newEater = eaters.getJSONObject(idx);
                Eater currentEater = new Eater();

                currentEater.setMeal(Integer.parseInt(newEater.getString("meal_id")));
                currentEater.setGuest(Integer.parseInt(newEater.getString("resident_id")));
                currentEater.setGuest_count(Integer.parseInt(newEater.getString("guest_count")));

                udb.addEater(currentEater);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e);
        }
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(MealInfoActivity.this, MealsActivity.class);
        MealInfoActivity.this.startActivity(myIntent);
        finish();
    }

    @Override
    protected void onResume() {
        layout();
        super.onResume();
    }
}
