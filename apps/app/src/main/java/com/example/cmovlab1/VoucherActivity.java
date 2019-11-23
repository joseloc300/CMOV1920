package com.example.cmovlab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class VoucherActivity extends AppCompatActivity {

    private ArrayList<String> vouchers = new ArrayList<>();
    VoucherListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Vouchers");
        setContentView(R.layout.activity_voucher);
        getVouchers();
        setupList();
    }

    private void setupList() {
        adapter = new VoucherListAdapter(this, android.R.layout.simple_list_item_1, vouchers);
        ListView list_vouchers = findViewById(R.id.lv_vouchers);
        list_vouchers.setAdapter(adapter);
    }

    private void getVouchers() {
        SharedPreferences keys = getSharedPreferences("keys", MODE_PRIVATE);
        RequestParams rq = new RequestParams();
        rq.add("user_uuid", keys.getString("user_uuid", ""));

        SharedPreferences server = getSharedPreferences("server", MODE_PRIVATE);
        String ip = server.getString("server_ip", "");

        ServerRestClient.post(ip, "/vouchers", rq, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray responseVouchers = response.getJSONArray("vouchers");
                        for(int i = 0; i < responseVouchers.length(); i++) {
                            adapter.add(responseVouchers.getString(i));
                        }
                    }
                    catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to retrieve vouchers from the server",Toast.LENGTH_SHORT).show();
                    }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response){
                Toast.makeText(getApplicationContext(),"Failed to retrieve vouchers from the server",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
