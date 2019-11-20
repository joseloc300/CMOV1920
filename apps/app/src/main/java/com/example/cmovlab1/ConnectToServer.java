package com.example.cmovlab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.*;
import org.json.*;
import cz.msebera.android.httpclient.*;

public class ConnectToServer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Connect to Server");
        setContentView(R.layout.activity_connect_to_server);
        bindHandlers();
    }

    private void bindHandlers() {
        confirmButtonHandler();
    }

    private void confirmButtonHandler() {
        Button btn_confirm_ip = (Button)findViewById(R.id.btn_confirm_ip);
        btn_confirm_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip_1 = ((EditText)findViewById(R.id.et_ip_1)).getText().toString();
                String ip_2 = ((EditText)findViewById(R.id.et_ip_2)).getText().toString();
                String ip_3 = ((EditText)findViewById(R.id.et_ip_3)).getText().toString();
                String ip_4 = ((EditText)findViewById(R.id.et_ip_4)).getText().toString();

                if(ip_1.equals("") || ip_2.equals("") || ip_3.equals("") || ip_4.equals("")) {
                    Toast.makeText(getApplicationContext(),"Please fill in all 4 fields.",Toast.LENGTH_SHORT).show();
                    return;
                }

                String ip = ip_1 + "." + ip_2 + "."+ ip_3 + "."+ ip_4;

                isServerAlive(ip);
            }
        });
    }

    private void isServerAlive(final String ip) {
        ServerRestClient.get(ip, "/isalive", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(statusCode == 200) {
                    SharedPreferences.Editor editor = getSharedPreferences("server", MODE_PRIVATE).edit();
                    editor.putString("server_ip", ip);
                    Boolean success = editor.commit();
                    if(!success) {
                        Toast.makeText(getApplicationContext(),"Error saving ip address. Please try again.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Connected to the server sucessfully.",Toast.LENGTH_SHORT).show();
                        callNextActivity();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response){
                Toast.makeText(getApplicationContext(),"Could not connect to the server.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callNextActivity() {
        //check if its 1st use of the app
        SharedPreferences keys = getSharedPreferences("keys", MODE_PRIVATE);
        if(!keys.contains("user_uuid")) {
            Intent register = new Intent(this, Register.class);
            startActivity(register);
        }
        else {
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        }

    }
}

