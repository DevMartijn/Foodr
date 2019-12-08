package com.example.unskilled.foodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.unskilled.foodr.API.DatabaseCommands;
import com.example.unskilled.foodr.Helpers.AlertDialogHelper;
import com.example.unskilled.foodr.Helpers.SavedDataHelper;
import com.example.unskilled.foodr.SQLite.DBHandler;
import com.example.unskilled.foodr.classes.Meal;

public class NewMealActivity extends AppCompatActivity {

    private EditText nameEditText, descriptionEditText, priceEditText, guestEditText, placeEditText, dateEditText, timeEditText, imageEditText;
    private Button saveButton;

    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);

        nameEditText = (EditText) findViewById(R.id.txt_new_meal_name);
        descriptionEditText = (EditText) findViewById(R.id.txt_new_meal_description);
        priceEditText = (EditText) findViewById(R.id.txt_new_meal_price);
        guestEditText = (EditText) findViewById(R.id.txt_new_meal_total_guest);
        placeEditText = (EditText) findViewById(R.id.txt_new_meal_place);
        dateEditText = (EditText) findViewById(R.id.txt_new_meal_date);
        timeEditText = (EditText) findViewById(R.id.txt_new_meal_time);
        imageEditText = (EditText) findViewById(R.id.txt_new_meal_image);

        saveButton = (Button) findViewById(R.id.btn_new_meal_save);

        dbHandler = new DBHandler(getApplicationContext(), null, null, 1);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Meal meal = new Meal();
                String image = null;

                if (nameEditText.getText().toString().length() < 2) {
                    AlertDialogHelper.errorDialog(NewMealActivity.this, "Naam error", "Naam is incorrect ingevoerd");
                }else if (guestEditText.getText().toString().length() < 1){
                    AlertDialogHelper.errorDialog(NewMealActivity.this, "Gast error", "Voer een correct aantal gasten in");
                }else if (priceEditText.getText().toString().length() < 1){
                    AlertDialogHelper.errorDialog(NewMealActivity.this, "Prijs error", "Voer een correcte prijs in");
                }else if ((dateEditText.getText().toString() + " " + timeEditText.getText().toString()).length() != 19) {
                    AlertDialogHelper.errorDialog(NewMealActivity.this, "Tijd en datum error", "Voer de tijd en datum in zoals aangegeven");
                } else {

                    String name = nameEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    double price = Double.valueOf(priceEditText.getText().toString());
                    int maxGuests = Integer.valueOf(guestEditText.getText().toString());
                    String place = placeEditText.getText().toString();
                    String time = dateEditText.getText().toString() + " " + timeEditText.getText().toString();
                    if (imageEditText.getText().toString().trim().length() > 0)
                        image = imageEditText.getText().toString();


                    meal.setName(nameEditText.getText().toString());
                    meal.setDescription(descriptionEditText.getText().toString());
                    meal.setPrice(Double.valueOf(priceEditText.getText().toString()));
                    meal.setMaxGuests(Integer.valueOf(guestEditText.getText().toString()));
                    meal.setPlace(placeEditText.getText().toString());
                    meal.setTime(time);
                    meal.setCook(SavedDataHelper.getCurrentUser().getId());

                    dbHandler.addMeal(meal);
                    DatabaseCommands.insertMeal(maxGuests, name, description, price, place, time, image);

                    Intent myIntent = new Intent(NewMealActivity.this, MealsActivity.class);
                    NewMealActivity.this.startActivity(myIntent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(NewMealActivity.this, MealsActivity.class);
        NewMealActivity.this.startActivity(myIntent);
        finish();
    }
}
