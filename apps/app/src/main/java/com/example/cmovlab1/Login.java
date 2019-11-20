package com.example.cmovlab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_login);
        bindHandlers();
    }

    private void bindHandlers() {
        loginButton();
    }

    private void loginButton() {
        Button login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences keys = getSharedPreferences("keys", MODE_PRIVATE);
                String stored_username = keys.getString("username", "");
                String stored_hash = keys.getString("user_hashed_pw", "");

                String username = ((EditText)findViewById(R.id.et_username)).getText().toString();
                String password = ((EditText)findViewById(R.id.et_pw)).getText().toString();

                if(checkPassword(password, stored_hash) && stored_username.equals(username)) {
                    switchToMainActivity();
                }
                else
                    Toast.makeText(getApplicationContext(),"Error logging in. Check your credentials.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean checkPassword(String pw, String stored_hash) {
        try {
            MessageDigest md = MessageDigest.getInstance( "SHA-256" );
            md.update( pw.getBytes( StandardCharsets.UTF_8 ) );
            String hashed_pw = new String(md.digest());

            return stored_hash.equals(hashed_pw);
        }
        catch (Exception e) {
            return false;
        }
    }

    private void switchToMainActivity() {
        Intent mainScreen = new Intent(this, MainScreen.class);
        startActivity(mainScreen);
    }

}
