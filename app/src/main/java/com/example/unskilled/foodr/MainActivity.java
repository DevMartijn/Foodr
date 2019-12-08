package com.example.unskilled.foodr;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.unskilled.foodr.API.DatabaseCommands;
import com.example.unskilled.foodr.Helpers.AlertDialogHelper;
import com.example.unskilled.foodr.Helpers.SavedDataHelper;
import com.example.unskilled.foodr.SQLite.DBHandler;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView createAccountText;

    private DBHandler udb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        emailEditText = (EditText) findViewById(R.id.txt_email);
        passwordEditText = (EditText) findViewById(R.id.txt_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        createAccountText = (TextView) findViewById(R.id.txt_create_account);


        udb = new DBHandler(getApplicationContext(), null, null, 1);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseCommands.login(emailEditText.getText().toString(),passwordEditText.getText().toString(), udb);
                if(SavedDataHelper.getToken() != "") {
                    Intent myIntent = new Intent(MainActivity.this, MealsActivity.class);
                    MainActivity.this.startActivity(myIntent);
                }else{
                    AlertDialogHelper.errorDialog(MainActivity.this, "Inlog error","Gebruikersnaam of wachtwoord is onjuist");
                }
            }
        });
        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, RegisterUser.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }
}
