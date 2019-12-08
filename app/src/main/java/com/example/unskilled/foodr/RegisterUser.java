package com.example.unskilled.foodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.unskilled.foodr.API.DatabaseCommands;
import com.example.unskilled.foodr.Helpers.AlertDialogHelper;

public class RegisterUser extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, passwordRepeatText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        firstNameEditText = (EditText) findViewById(R.id.txt_register_first_name);
        lastNameEditText = (EditText) findViewById(R.id.txt_register_last_name);
        emailEditText = (EditText) findViewById(R.id.txt_register_email);
        passwordEditText = (EditText) findViewById(R.id.txt_register_password);
        passwordRepeatText = (EditText) findViewById(R.id.txt_register_email);

        registerButton = (Button) findViewById(R.id.btn_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordRepeat = passwordRepeatText.getText().toString();

                if(firstName.trim().length() < 2){
                    AlertDialogHelper.errorDialog(RegisterUser.this, "Voornaam error","Voornaam is incorrect ingevoerd");
                }else if(lastName.trim().length() < 2){
                    AlertDialogHelper.errorDialog(RegisterUser.this, "Achternaam error","Achternaam is incorrect ingevoerd");
                }else if(email.trim().length() < 2){
                    AlertDialogHelper.errorDialog(RegisterUser.this, "Email error","Email is incorrect ingevoerd");
                }else if(password.trim().length() < 2){
                    AlertDialogHelper.errorDialog(RegisterUser.this, "Wachtwoord error","Het wachtwoord is incorrect ingevoerd");
                }else {
                    DatabaseCommands.registerUser(email, password, firstName, lastName);
                    finish();
                    AlertDialogHelper.errorDialog(RegisterUser.this, "Gebruiker aangemaakt","De gebruiker is succesvol aangemaakt");
                }
            }
        });
    }
}
