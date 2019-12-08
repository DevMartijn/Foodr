package com.example.unskilled.foodr.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.unskilled.foodr.classes.Eater;
import com.example.unskilled.foodr.classes.Meal;
import com.example.unskilled.foodr.classes.User;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "userDBHandler";

    //Database info
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "foodr.db";

    //TABELS
    private static final String DB_TABLE_NAME = "users";
    private static final String DB_TABLE_MEAL = "meals";
    private static final String DB_TABLE_EATER = "eaters";

    //USER
    private static final String COLUMN_ID = "_id";  // primary key, auto increment
    private static final String COLUMN_FIRSTNAME = "firstName";
    private static final String COLUMN_LASTNAME = "lastName";
    private static final String COLUMN_EMAIL = "email";

    // MEAL
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_MAX_GUESTS = "maxGuests";
    private static final String COLUMN_PLACE = "place";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_TIME = "dateTime";
    private static final String COLUMN_COOK = "cook";
    private static final String COLUMN_MEAL_ID = "mealId";

    //EATER
    private static final String COLUMN_USER_ID = "userId";
    private static final String COLUMN_GUEST_COUNT = "guestCount";



    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PERSON_TABLE = "CREATE TABLE " + DB_TABLE_NAME +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_FIRSTNAME + " TEXT," +
                COLUMN_LASTNAME + " TEXT," +
                COLUMN_EMAIL + " TEXT" +
                ")";

        String CREATE_MEALS_TABLE = "CREATE TABLE " + DB_TABLE_MEAL +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_PRICE + " REAL," +
                COLUMN_MAX_GUESTS + " INTEGER," +
                COLUMN_PLACE + " TEXT," +
                COLUMN_IMAGE + " TEXT," +
                COLUMN_TIME + " TEXT," +
                COLUMN_COOK + " INTEGER," +
                COLUMN_MEAL_ID + " INTEGER" +
                ")";

        String CREATE_EATERS_TABLE = "CREATE TABLE " + DB_TABLE_EATER +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_MEAL_ID + " INTEGER," +
                COLUMN_USER_ID + " INTEGER," +
                COLUMN_GUEST_COUNT + " INTEGER" +
                ")";


        db.execSQL(CREATE_PERSON_TABLE);
        db.execSQL(CREATE_MEALS_TABLE);
        db.execSQL(CREATE_EATERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_MEAL);
        onCreate(db);
    }

    //--- USERS ---

    public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, user.getId());
        values.put(COLUMN_FIRSTNAME, user.getFirstName());
        values.put(COLUMN_LASTNAME, user.getLastName());
        values.put(COLUMN_EMAIL, user.getEmail());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DB_TABLE_NAME, null, values);
        db.close();
    }


    public void deleteAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ DB_TABLE_NAME);
        db.close();
    }

    public User getUserById(int id) {
        User user = new User();

        String query_b = "SELECT * FROM " + DB_TABLE_NAME + " WHERE _id = " + id + "";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(query_b, null);
        c.moveToFirst();
        user = curserToUser(c);

        db.close();

        return user;
    }

    // --- MEALS ---

    public void addMeal(Meal meal) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, meal.getName());
        values.put(COLUMN_DESCRIPTION, meal.getDescription());
        values.put(COLUMN_PRICE, meal.getPrice());
        values.put(COLUMN_MAX_GUESTS, meal.getMaxGuests());
        values.put(COLUMN_PLACE, meal.getDescription());
        values.put(COLUMN_IMAGE, meal.getImage());
        values.put(COLUMN_TIME, meal.getTime());
        values.put(COLUMN_COOK, meal.getCook());
        values.put(COLUMN_MEAL_ID, meal.getId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DB_TABLE_MEAL, null, values);
        db.close();
    }

    public Meal getMealById(int id) {

        Meal meal = new Meal();

        String query_b = "SELECT * FROM " + DB_TABLE_MEAL + " WHERE " + COLUMN_MEAL_ID + " = " + id + "";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(query_b, null);
        c.moveToFirst();
        meal = curserToMeal(c);

        db.close();

        return meal;
    }

    public ArrayList<Meal> getAllMeals() {
        String query_b = "SELECT * FROM " + DB_TABLE_MEAL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query_b, null);

        ArrayList<Meal> meals = new ArrayList<Meal>();
        while (cursor.moveToNext()) {
            meals.add(curserToMeal(cursor));
        }

        db.close();


        return meals;
    }

    public void deleteAllMeals(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ DB_TABLE_MEAL);
        db.close();
    }

    //--- EATERS ---
    public void deleteAllEaters(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ DB_TABLE_EATER);
        db.close();
    }

    public void addEater(Eater eater){
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEAL_ID, eater.getMeal());
        values.put(COLUMN_USER_ID, eater.getGuest());
        values.put(COLUMN_GUEST_COUNT, eater.getGuest_count());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DB_TABLE_EATER, null, values);
        db.close();
    }

    public ArrayList<Eater> getAllEatersByMealId(int id) {

        ArrayList<Eater> eaters = new ArrayList<Eater>();

        String query_b = "SELECT * FROM " + DB_TABLE_EATER + " WHERE " + COLUMN_MEAL_ID + "= " + id;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query_b, null);

        while (cursor.moveToNext()) {
            eaters.add(curserToEater(cursor));
        }

        db.close();

        return eaters;
    }

    private Meal curserToMeal(Cursor cursor) {
        Meal meal = new Meal();
        meal.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_MEAL_ID)));
        meal.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        meal.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
        meal.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)));
        meal.setMaxGuests(cursor.getInt(cursor.getColumnIndex(COLUMN_MAX_GUESTS)));
        meal.setPlace(cursor.getString(cursor.getColumnIndex(COLUMN_PLACE)));
        meal.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
        meal.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
        meal.setCook(cursor.getInt(cursor.getColumnIndex(COLUMN_COOK)));
        return meal;
    }

    private User curserToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        user.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FIRSTNAME)));
        user.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LASTNAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
        return user;
    }

    private Eater curserToEater(Cursor cursor){
        Eater eater = new Eater();
        eater.setMeal(cursor.getInt(cursor.getColumnIndex(COLUMN_MEAL_ID)));
        eater.setGuest(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
        eater.setGuest_count(cursor.getInt(cursor.getColumnIndex(COLUMN_GUEST_COUNT)));
        return eater;
    }
}
