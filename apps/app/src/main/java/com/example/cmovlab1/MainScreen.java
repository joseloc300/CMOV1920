package com.example.cmovlab1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ACME Supermarket");
        setContentView(R.layout.activity_main_screen);
        bindHandlers();
    }

    private void bindHandlers() {
        shoppingCartButtonHandler();
        couponsButtonHandler();
        transactionsButtonHandler();
        editProfileButtonHandler();
        checkoutButtonHandler();
    }

    private void shoppingCartButtonHandler() {

    }

    private void couponsButtonHandler() {

    }

    private void transactionsButtonHandler() {

    }

    private void editProfileButtonHandler() {

    }

    private void checkoutButtonHandler() {

    }
}
