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

public class TransactionsActivity extends AppCompatActivity {

    private ArrayList<Transaction> transactions = new ArrayList<>();
    TransactionListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Transactions");
        setContentView(R.layout.activity_transactions);
        getTransactions();
        setupList();
    }

    private void setupList() {
        adapter = new TransactionListAdapter(this, android.R.layout.simple_list_item_1, transactions);
        ListView list_transactions = findViewById(R.id.lv_transactions);
        list_transactions.setAdapter(adapter);
    }

    private void getTransactions() {
        SharedPreferences keys = getSharedPreferences("keys", MODE_PRIVATE);
        RequestParams rq = new RequestParams();
        rq.add("user_uuid", keys.getString("user_uuid", ""));

        SharedPreferences server = getSharedPreferences("server", MODE_PRIVATE);
        String ip = server.getString("server_ip", "");

        ServerRestClient.post(ip, "/transactions", rq, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray responseVouchers = response.getJSONArray("transactions");
                    for(int i = 0; i < responseVouchers.length(); i++) {
                        String transaction = responseVouchers.getString(i);
                        JSONObject transactionJSON = new JSONObject(transaction);

                        String price = transactionJSON.getString("total");
                        String used_voucher = transactionJSON.getString("used_voucher");
                        String used_discount = transactionJSON.getString("used_discount");
                        String items = transactionJSON.getString("items");

                        Transaction t = new Transaction(price, items, used_voucher, used_discount);
                        adapter.add(t);
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
