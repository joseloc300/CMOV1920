package com.example.cmovlab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if its 1st use of the app
        SharedPreferences keys = getSharedPreferences("keys", MODE_PRIVATE);
        if(!keys.contains("user_uuid")) {

            //clear activity stack (prevent going back to login
            //switch to register activity
            Intent register = new Intent(this, Register.class);
            register.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(register);

            Log.d("myTag", "entrou");
        }

        setContentView(R.layout.activity_login);

    }
}
