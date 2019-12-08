package com.example.unskilled.foodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.unskilled.foodr.API.DatabaseCommands;
import com.example.unskilled.foodr.Helpers.AlertDialogHelper;
import com.example.unskilled.foodr.Helpers.SavedDataHelper;

public class MealInfoPopUp extends AppCompatActivity {

    private TextView totalGuests;
    private Button saveMeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_info_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * .8),(int) (315));

        final int meal_id = getIntent().getIntExtra("MEAL_ID",0);
        final int total_guests = getIntent().getIntExtra("TOTAL_GUEST",0);
        final int user_id = SavedDataHelper.getCurrentUser().getId();

        final int checkInCount = DatabaseCommands.getCountEaters(meal_id);

        totalGuests = (TextView) findViewById(R.id.meal_info_pop_up_guests);
        saveMeal = (Button) findViewById(R.id.meal_info_pop_up_button);

        saveMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalGuests.getText().toString().trim().length() < 0){
                    AlertDialogHelper.errorDialog(MealInfoPopUp.this, "Gasten error","Voer een correct aantal gasten in");
                }else {
                    int iTotalGuests = Integer.parseInt(totalGuests.getText().toString());
                    if(checkInCount + iTotalGuests + 1 > total_guests) {
                        AlertDialogHelper.errorDialog(MealInfoPopUp.this, "Gasten error","Je hebt teveel gasten meegenomen");
                    }else{
                        DatabaseCommands.insertEatMeal(meal_id, user_id, iTotalGuests);
                        finish();
                    }
                }
            }
        });

    }
}
