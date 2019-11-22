package com.example.cmovlab1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Register");
        setContentView(R.layout.activity_register);
        bindHandlers();
    }

    public void bindHandlers() {
        registerHandler();
    }

    public void registerHandler() {
        Button btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText)findViewById(R.id.et_username)).getText().toString();
                String password = ((EditText)findViewById(R.id.et_pw)).getText().toString();
                String fullname = ((EditText)findViewById(R.id.et_fullname)).getText().toString();
                String card_number = ((EditText)findViewById(R.id.et_cardnumber)).getText().toString();
                String card_expiration = ((EditText)findViewById(R.id.et_expdate)).getText().toString();
                String card_cv2 = ((EditText)findViewById(R.id.et_cv2)).getText().toString();

                if(username.equals("") || password.equals("") || fullname.equals("") || card_number.equals("") || card_expiration.equals("") || card_cv2.equals("")) {
                    Toast.makeText(getApplicationContext(),"Please fill in all fields.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length() < 8) {
                    Toast.makeText(getApplicationContext(),"Password must be at least 8 characters long.",Toast.LENGTH_SHORT).show();
                    return;
                }

                KeyPair kp = null;
                String hashed_pw = "";

                try {
                    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                    kpg.initialize(512);
                    kp = kpg.generateKeyPair();

                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Error while generating the client's keys",Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    MessageDigest md = MessageDigest.getInstance( "SHA-256" );
                    md.update( password.getBytes( StandardCharsets.UTF_8 ) );
                    hashed_pw = new String(md.digest());
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Error hashing password",Toast.LENGTH_SHORT).show();
                    return;
                }

                String publicKey = Base64.encodeToString(kp.getPublic().getEncoded(), Base64.DEFAULT);
                String privateKey = Base64.encodeToString(kp.getPrivate().getEncoded(), Base64.DEFAULT);

                SharedPreferences.Editor editor = getSharedPreferences("keys", MODE_PRIVATE).edit();
                editor.putString("user_private_key", privateKey);
                editor.putString("user_public_key", publicKey);
                editor.putString("user_hashed_pw", hashed_pw);
                editor.putString("username", username);
                Boolean success = editor.commit();
                if(!success) {
                    Toast.makeText(getApplicationContext(),"Error storing the client's keys",Toast.LENGTH_SHORT).show();
                }
                else {
                    registeruser(username, fullname, card_number, card_expiration, card_cv2, publicKey);
                }
            }
        });
    }

    private void registeruser(String username, String fullname, String card_number, String card_expiration, String card_cv2, String public_key) {
        RequestParams rq = new RequestParams();
        rq.add("username", username);
        rq.add("fullname", fullname);
        rq.add("card_number", card_number);
        rq.add("card_expiration", card_expiration);
        rq.add("card_cv2", card_cv2);
        rq.add("public_key", public_key);
        rq.add("username", username);

        SharedPreferences server = getSharedPreferences("server", MODE_PRIVATE);
        String ip = server.getString("server_ip", "");

        ServerRestClient.post(ip, "/users/register", rq, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(statusCode == 201) {
                    try {
                        SharedPreferences.Editor editor = getSharedPreferences("keys", MODE_PRIVATE).edit();
                        editor.putString("user_uuid", response.getString("user_uuid"));
                        editor.putString("server_public_key", response.getString("server_public_key"));
                        Boolean success = editor.commit();
                        if(!success) {
                            Toast.makeText(getApplicationContext(),"Failed to register in the server",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Registered sucessfully.",Toast.LENGTH_SHORT).show();
                            callNextActivity();
                        }
                    }
                    catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to register in the server",Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response){
                Toast.makeText(getApplicationContext(),"Failed to register in the server",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callNextActivity() {
        Intent mainScreen = new Intent(this, MainScreen.class);
        startActivity(mainScreen);
        this.finish();
    }
}
