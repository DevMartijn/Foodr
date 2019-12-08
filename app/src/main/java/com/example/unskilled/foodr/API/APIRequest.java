package com.example.unskilled.foodr.API;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


public class APIRequest {

    private static final String TAG = "APIRequest";
    private static final String URL = "https://eiin-vprogr4.herokuapp.com/";

    private static String makePOST(String endpoint, Map<String, Object> params){
        try {
            URL api_url = new URL(URL+endpoint);
            StringBuilder postData = new StringBuilder();
            for(Map.Entry<String, Object> param : params.entrySet()){
                if(postData.length() > 0) postData.append("&");
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append("=");
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            HttpURLConnection conn = (HttpURLConnection) api_url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            conn.disconnect();
            return stringBuilder.toString();
        }
        catch (Exception e){
            Log.e(TAG, "Object error: " + e);
            return null;
        }
    }

    public static Object POST(String endpoint, Map<String, Object> params){
        String result = makePOST(endpoint, params);
        try{
            if(result.startsWith("{")){
                return new JSONObject(result);
            }else if(result.startsWith("[")){
                return new JSONArray(result);
            }else{
                throw new Exception("Error");
            }
        }catch(Exception e){
            Log.e(TAG, "Object error: " + e);
            return null;
        }
    }

    public static String makeGET(String endpoint, Map<String, Object> params){
        try{
            StringBuilder headers = new StringBuilder();

            for(Map.Entry<String, Object> param : params.entrySet()){
                if(headers.length() > 0) headers.append("&");
                headers.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                headers.append("=");
                headers.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            URL api_url = new URL(URL+endpoint+"?"+headers.toString());
            HttpURLConnection conn = (HttpURLConnection) api_url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            conn.disconnect();
            bufferedReader.close();
            return stringBuilder.toString();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Object GET(String endpoint, Map<String, Object> params){
        String result = makeGET(endpoint, params);
        try{
            if(result.startsWith("{")){
                return new JSONObject(result);
            }else if(result.startsWith("[")){
                return new JSONArray(result);
            }else{
                throw new Exception("Error");
            }
        }catch(Exception e){
            Log.e(TAG, "Object error: " + e);
            return null;
        }
    }
}
