package com.example.cmovlab1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.net.*;
import java.io.*;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register() {

    }

    private class RegisterUser implements Runnable {
        String address = null; String uname = null;
        RegisterUser(String baseAddress, String userName) {
            address = baseAddress; uname = userName;
        }
        @Override public void run() {
            /*
            URL url; HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://" + address + ":8701/Rest/users");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setUseCaches (false);
                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                String payload = "\"" + uname + "\"";
                outputStream.writeBytes(payload);
                outputStream.flush();
                outputStream.close();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200)
                    String response = readStream(urlConnection.getInputStream()); // … and transmit to UI
            }
            catch (Exception e) {
                // treat the exception
            }
            finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }*/
        } }
}
