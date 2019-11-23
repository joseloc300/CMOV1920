package com.example.cmovlab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CheckoutActivity extends AppCompatActivity {

    private Boolean useVoucher = false;
    private Boolean useStoredDiscount = false;
    private int voucherCount = 0;
    private int storedDiscount = 0;
    private int totalPriceEuros = 0;
    private int totalPriceCents = 0;
    private String voucher_to_use = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Checkout");
        setContentView(R.layout.activity_checkout);
        setupVars();
        bindHandlers();
    }

    private void setupVars() {
        setupPriceVars();
        requestServerInfo();
    }

    private void setupPriceVars() {
        int tempPrice = 0;
        for(int i = 0; i < ShoppingList.items.size(); i++) {
            tempPrice += ShoppingList.items.get(i).getEuros() * 100;
            tempPrice += ShoppingList.items.get(i).getCents();
        }

        totalPriceCents = tempPrice % 100;
        totalPriceEuros = tempPrice / 100;
    }

    private void requestServerInfo() {
        SharedPreferences keys = getSharedPreferences("keys", MODE_PRIVATE);
        RequestParams rq = new RequestParams();
        rq.add("user_uuid", keys.getString("user_uuid", ""));

        SharedPreferences server = getSharedPreferences("server", MODE_PRIVATE);
        String ip = server.getString("server_ip", "");
        ServerRestClient.get(ip, "/checkoutInfo", rq, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    voucherCount = Integer.parseInt(response.getString("total"));
                    TextView tv_checkout_vouchers = findViewById(R.id.tv_checkout_vouchers);
                    tv_checkout_vouchers.setText("You currently have " + voucherCount + " unused vouchers.");

                    if( voucherCount == 0) {
                        CheckBox cbVoucher = findViewById(R.id.cb_use_voucher);
                        cbVoucher.setEnabled(false);
                    }
                    else {
                        voucher_to_use = response.getString("voucher");
                    }
                    storedDiscount = Integer.parseInt(response.getString("storedDiscount"));
                    TextView tv_total_stored_discount = findViewById(R.id.tv_total_stored_discount);
                    int discoutEuros = storedDiscount / 100;
                    int discountCents = storedDiscount % 100;
                    tv_total_stored_discount.setText("You currently have " + discoutEuros + "." + discountCents + "€ stored discount.");

                    if( storedDiscount == 0) {
                        CheckBox cb_useDiscount = findViewById(R.id.cb_useDiscount);
                        cb_useDiscount.setEnabled(false);
                    }
                }
                catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response){
                Toast.makeText(getApplicationContext(),"Failed to request info from the server",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindHandlers() {
        onVoucherCheckBoxClick();
        onDiscountCheckBoxClick();
        onCheckoutButtonClick();
    }

    private void onVoucherCheckBoxClick() {
        CheckBox cbVoucher = findViewById(R.id.cb_use_voucher);
        cbVoucher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                useVoucher = isChecked;
                Log.d("cb", Boolean.toString(isChecked));
            }
        });
    }

    private void onDiscountCheckBoxClick() {
        CheckBox cbDiscount = findViewById(R.id.cb_useDiscount);
        cbDiscount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                useStoredDiscount = isChecked;
                Log.d("cb", Boolean.toString(isChecked));
            }
        });
    }

    private void onCheckoutButtonClick() {
        SharedPreferences keys = getSharedPreferences("keys", MODE_PRIVATE);
        RequestParams rq = new RequestParams();
        rq.add("user_uuid", keys.getString("user_uuid", ""));
        rq.add("useDiscount", useStoredDiscount ? "Y" : "N");
        rq.add("voucher", voucher_to_use == null ? "null" : voucher_to_use);
        rq.add("items", ShoppingList.itemListToJSON().toString());

        SharedPreferences server = getSharedPreferences("server", MODE_PRIVATE);
        String ip = server.getString("server_ip", "");
        ServerRestClient.get(ip, "/checkout", rq, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    voucherCount = Integer.parseInt(response.getString("total"));
                    TextView tv_checkout_vouchers = findViewById(R.id.tv_checkout_vouchers);
                    tv_checkout_vouchers.setText("You currently have " + voucherCount + " unused vouchers.");

                    if( voucherCount == 0) {
                        CheckBox cbVoucher = findViewById(R.id.cb_use_voucher);
                        cbVoucher.setEnabled(false);
                    }
                    else {
                        voucher_to_use = response.getString("voucher");
                    }
                    storedDiscount = Integer.parseInt(response.getString("storedDiscount"));
                    TextView tv_total_stored_discount = findViewById(R.id.tv_total_stored_discount);
                    int discoutEuros = storedDiscount / 100;
                    int discountCents = storedDiscount % 100;
                    tv_total_stored_discount.setText("You currently have " + discoutEuros + "." + discountCents + "€ stored discount.");

                    if( storedDiscount == 0) {
                        CheckBox cb_useDiscount = findViewById(R.id.cb_useDiscount);
                        cb_useDiscount.setEnabled(false);
                    }
                }
                catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response){
                Toast.makeText(getApplicationContext(),"Failed to request info from the server",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
