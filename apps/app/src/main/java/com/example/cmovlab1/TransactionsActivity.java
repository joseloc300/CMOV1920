package com.example.cmovlab1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class TransactionsActivity extends AppCompatActivity {

    public static ArrayList<Item> items = new ArrayList<>();
    ItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Transactions");
        setContentView(R.layout.activity_transactions);
    }
}
