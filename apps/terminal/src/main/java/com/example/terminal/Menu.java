package com.example.terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Terminal Menu");
        setContentView(R.layout.activity_menu);
        bindHandlers();
    }

    private void bindHandlers() {
        itemListButtonHandler();
        checkoutButtonHandler();
    }

    private void itemListButtonHandler() {
        Button btn_shoppingCart = (Button)findViewById(R.id.btn_itemlist);
        btn_shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToItemListActivity();
            }
        });
    }

    private void checkoutButtonHandler() {
        Button btn_coupons = (Button)findViewById(R.id.btn_checkout);
        btn_coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToCheckoutActivity();
            }
        });
    }

    private void switchToItemListActivity() {
        Intent itemList = new Intent(this, ItemListActivity.class);
        startActivity(itemList);
    }

    private void switchToCheckoutActivity() {
        Intent checkout = new Intent(this, CheckoutActivity.class);
        startActivity(checkout);
    }
}
