package com.example.unskilled.foodr.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.unskilled.foodr.R;
import com.example.unskilled.foodr.SQLite.DBHandler;
import com.example.unskilled.foodr.classes.Eater;
import com.example.unskilled.foodr.classes.User;

import java.util.ArrayList;

public class EatersAdapter extends BaseAdapter {


    // TAG for Log.i(...)
    private static final String TAG = MealAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mInflator;
    private ArrayList<Eater> eaterArrayList;
    private DBHandler db;

    public EatersAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Eater> eatersArrayList, DBHandler db)
    {
        mContext = context;
        mInflator = layoutInflater;
        this.eaterArrayList = eatersArrayList;
        this.db = db;
    }

    @Override
    public int getCount() {
        return eaterArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return eaterArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        EatersAdapter.ViewHolder viewHolder;

        if(view == null) {

            view = mInflator.inflate(R.layout.eater_listview, null);

            viewHolder = new EatersAdapter.ViewHolder();
            viewHolder.eaterName = (TextView) view.findViewById(R.id.eater_listview_name);
            viewHolder.eaterEmail = (TextView) view.findViewById(R.id.eater_listview_email);
            viewHolder.totalGuests = (TextView) view.findViewById(R.id.eater_listview_guests);

            view.setTag(viewHolder);
        } else {
            viewHolder = (EatersAdapter.ViewHolder) view.getTag();
        }

        User user = db.getUserById(eaterArrayList.get(i).getGuest());

        viewHolder.eaterName.setText(user.getFirstName() + " " + user.getLastName());
        viewHolder.eaterEmail.setText(user.getEmail());
        viewHolder.totalGuests.setText("Aantal gasten: " + eaterArrayList.get(i).getGuest_count());
        return view;
    }

    // Holds all data to the view. Wordt evt. gerecycled door Android
    private static class ViewHolder {
        public TextView eaterName;
        public TextView eaterEmail;
        public TextView totalGuests;
    }
}
