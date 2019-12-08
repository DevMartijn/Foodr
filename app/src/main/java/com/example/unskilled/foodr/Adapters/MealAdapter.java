package com.example.unskilled.foodr.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.unskilled.foodr.MealInfoActivity;
import com.example.unskilled.foodr.R;
import com.example.unskilled.foodr.SQLite.DBHandler;
import com.example.unskilled.foodr.classes.Meal;
import com.example.unskilled.foodr.classes.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MealAdapter extends BaseAdapter {

    // TAG for Log.i(...)
    private static final String TAG = MealAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mInflator;
    private ArrayList<Meal> mealArrayList;
    private DBHandler db;

    public MealAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Meal> personArrayList, DBHandler db)
    {
        mContext = context;
        mInflator = layoutInflater;
        mealArrayList = personArrayList;
        this.db = db;
    }

    @Override
    public int getCount() {
        return mealArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mealArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(view == null) {

            view = mInflator.inflate(R.layout.meal_listview, null);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.personRowImageView);
            viewHolder.layout = (LinearLayout) view.findViewById(R.id.meal_listview_layout);
            viewHolder.mealName = (TextView) view.findViewById(R.id.meal_listview_meal);
            viewHolder.mealChef = (TextView) view.findViewById(R.id.meal_listview_chef);
            viewHolder.mealTime = (TextView) view.findViewById(R.id.meal_listview_time);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        User cook = db.getUserById(mealArrayList.get(i).getCook());


        if(mealArrayList.get(i).getImage() != null){
            Picasso.with(mContext).load(mealArrayList.get(i).getImage())
                    .fit()
                    .centerCrop()
                    .into(viewHolder.imageView);
        }

        viewHolder.mealName.setText(mealArrayList.get(i).getName());
        viewHolder.mealChef.setText(cook.getFirstName() + " " + cook.getLastName());
        viewHolder.mealTime.setText(mealArrayList.get(i).getTime());
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MealInfoActivity.class);
                intent.putExtra("MEAL_ID", mealArrayList.get(i).getId());
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    // Holds all data to the view. Wordt evt. gerecycled door Android
    private static class ViewHolder {
        public ImageView imageView;
        public LinearLayout layout;
        public TextView mealName;
        public TextView mealChef;
        public TextView mealTime;
    }
}
