package com.example.unskilled.foodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.unskilled.foodr.Adapters.EatersAdapter;
import com.example.unskilled.foodr.SQLite.DBHandler;
import com.example.unskilled.foodr.classes.Eater;

import java.util.ArrayList;

public class EatersActivity extends AppCompatActivity {

    private ListView eatersListView = null;
    private EatersAdapter eatersAdapter = null;

    private ArrayList<Eater> eaters = new ArrayList<Eater>();

    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eaters);

        final int meal_id = getIntent().getIntExtra("MEAL_ID",0);

        db = new DBHandler(getApplicationContext(), null, null, 1);

        eaters = db.getAllEatersByMealId(meal_id);

        eatersListView = (ListView) findViewById(R.id.eaters_listview);

        eatersAdapter = new EatersAdapter(getApplicationContext(), getLayoutInflater(), eaters, db);
        eatersListView.setAdapter(eatersAdapter);
    }
}
