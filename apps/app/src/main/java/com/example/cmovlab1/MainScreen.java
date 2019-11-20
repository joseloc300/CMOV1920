package com.example.cmovlab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        checkoutButtonHandler();
    }

    private void shoppingCartButtonHandler() {
        Button btn_shoppingCart = (Button)findViewById(R.id.btn_cart);
        btn_shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToCartActivity();
            }
        });
    }

    private void couponsButtonHandler() {
        switchToCouponsActivity();
    }

    private void transactionsButtonHandler() {
        switchToTransactionsActivity();
    }

    private void checkoutButtonHandler() {
        switchToCheckoutActivity();
    }

    private void switchToCartActivity() {
        Intent cart = new Intent(this, ShoppingList.class);
        startActivity(cart);
    }

    private void switchToTransactionsActivity() {
        Intent cart = new Intent(this, ShoppingList.class);
        startActivity(cart);
    }

    private void switchToCouponsActivity() {
        Intent cart = new Intent(this, ShoppingList.class);
        startActivity(cart);
    }

    private void switchToCheckoutActivity() {
        Intent cart = new Intent(this, ShoppingList.class);
        startActivity(cart);
    }
}
