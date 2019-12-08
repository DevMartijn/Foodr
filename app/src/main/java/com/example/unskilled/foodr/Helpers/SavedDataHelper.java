package com.example.unskilled.foodr.Helpers;

import android.util.Log;

import com.example.unskilled.foodr.classes.User;


public class SavedDataHelper {

    private static User currentUser;
    private static String token = "";

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        SavedDataHelper.token = token;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        SavedDataHelper.currentUser = currentUser;
    }

}
