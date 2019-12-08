package com.example.unskilled.foodr.API;

import android.os.StrictMode;
import android.util.Log;

import com.example.unskilled.foodr.SQLite.DBHandler;
import com.example.unskilled.foodr.Helpers.SavedDataHelper;
import com.example.unskilled.foodr.classes.Meal;
import com.example.unskilled.foodr.classes.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatabaseCommands {

    private static final String TAG = "DatabaseCommands";

    public static void insertMeal(int maxGuests, String name, String description, double price, String place, String dateTime, String imageURL){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", SavedDataHelper.getToken());
        map.put("cook", SavedDataHelper.getCurrentUser().getId());
        map.put("max_guests", maxGuests);
        map.put("name", name);
        map.put("price", price);
        map.put("place", place);
        map.put("time", dateTime);
        map.put("description", description);
        map.put("image", imageURL);
    }

    public static void insertEatMeal(int mealId, int residentId, int guestCount){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", SavedDataHelper.getToken());
        map.put("meal_id", mealId);
        map.put("resident_id", residentId);
        map.put("guest_count", guestCount);

        APIRequest.POST("meals/createEatMeal", map);
    }

    public static void login(String email, String password, DBHandler udb) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("email", email);
            map.put("password", password);

            JSONObject token = (JSONObject) APIRequest.POST("login", map);
            SavedDataHelper.setToken(token.getString("token"));
            SavedDataHelper.setCurrentUser(udb.getUserById(token.getInt("user_id")));
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e);
        }
    }

    public static void registerUser(String email, String password, String firstName, String lastName){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("firstname", firstName);
        map.put("lastname", lastName);
        map.put("email", email);
        map.put("password", password);

        APIRequest.POST("register", map);
    }

    public static void updateAllUsers(DBHandler udb){
        // Connect and pass self for callback
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", SavedDataHelper.getToken());

        udb.deleteAllUsers();

        JSONArray users = (JSONArray) APIRequest.GET("users", map);
        try {

            for (int idx = 0; idx < users.length(); idx++) {
                JSONObject currentUser = users.getJSONObject(idx);
                User newUser = new User();


                newUser.setId(Integer.parseInt(currentUser.getString("id")));
                newUser.setFirstName(currentUser.getString("firstname"));
                newUser.setLastName(currentUser.getString("lastname"));
                newUser.setEmail(currentUser.getString("email"));

                udb.addUser(newUser);

            }
        }catch (Exception e){
            Log.e(TAG, "Error: " + e);
        }
    }

    public static void updateAllMeals(DBHandler udb){
        // Connect and pass self for callback
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", SavedDataHelper.getToken());


        udb.deleteAllMeals();
        JSONArray meals = (JSONArray) APIRequest.GET("meals", map);
        try {

            for (int idx = 0; idx < meals.length(); idx++) {

                JSONObject newMeal = meals.getJSONObject(idx);
                Meal superNewMeal = new Meal();


                superNewMeal.setCook(Integer.parseInt(newMeal.getString("cook")));
                superNewMeal.setId(Integer.parseInt(newMeal.getString("id")));
                superNewMeal.setMaxGuests(Integer.parseInt(newMeal.getString("max_guests")));
                superNewMeal.setName(newMeal.getString("name"));
                superNewMeal.setDescription(newMeal.getString("description"));
                superNewMeal.setPrice(Double.parseDouble(newMeal.getString("price")));
                superNewMeal.setImage(newMeal.getString("image"));
                superNewMeal.setPlace(newMeal.getString("place"));
                superNewMeal.setTime(newMeal.getString("time"));

                udb.addMeal(superNewMeal);
            }
        }catch (Exception e){
            Log.e(TAG, "Error: " + e);
        }
    }

    public static int getCountEaters(int id){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("token", SavedDataHelper.getToken());

            JSONObject jsonObject = (JSONObject) APIRequest.GET("getCountEaters/" + id, map);
            int count = jsonObject.getInt("count");

            return count;
        }catch (Exception e){
            Log.e(TAG, "Error: " + e);
            return 0;
        }
    }

    public static void deleteMeal(int id){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", SavedDataHelper.getToken());
        map.put("meal_id", id);
        map.put("resident_id", SavedDataHelper.getCurrentUser().getId());
    }
}
