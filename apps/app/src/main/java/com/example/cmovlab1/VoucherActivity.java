package com.example.cmovlab1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class VoucherActivity extends AppCompatActivity {

    public static ArrayList<String> vouchers = new ArrayList<>();
    VoucherListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Vouchers");
        setContentView(R.layout.activity_voucher);
    }
}
