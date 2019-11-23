package com.example.cmovlab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;


import cz.msebera.android.httpclient.Header;

public class CheckoutActivity extends AppCompatActivity {

    private Boolean useVoucher = false;
    private Boolean useStoredDiscount = false;
    private int voucherCount = 0;
    private int storedDiscount = 0;
    private int totalPriceEuros = 0;
    private int totalPriceCents = 0;
    private String voucher_to_use = null;

    private final int DIMENSION = 500;
    private final static String CH_SET="ISO-8859-1";


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

        TextView tv_total_price_checkout = findViewById(R.id.tv_total_price_checkout);
        tv_total_price_checkout.setText("Total price: " + totalPriceEuros + "." + totalPriceCents + "€.");
    }

    private void requestServerInfo() {
        SharedPreferences keys = getSharedPreferences("keys", MODE_PRIVATE);
        RequestParams rq = new RequestParams();
        rq.add("user_uuid", keys.getString("user_uuid", ""));

        SharedPreferences server = getSharedPreferences("server", MODE_PRIVATE);
        String ip = server.getString("server_ip", "");
        ServerRestClient.post(ip, "/checkoutInfo", rq, new JsonHttpResponseHandler() {
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
        Button btn_confirm_checkout = findViewById(R.id.btn_confirm_checkout);
        btn_confirm_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject checkoutInfo = new JSONObject();
                    SharedPreferences keys = getSharedPreferences("keys", MODE_PRIVATE);
                    checkoutInfo.put("user_uuid", keys.getString("user_uuid", ""));
                    checkoutInfo.put("useDiscount", useStoredDiscount ? "Yes" : "No");
                    checkoutInfo.put("voucher", voucher_to_use == null ? "null" : voucher_to_use);
                    checkoutInfo.put("items", ShoppingList.itemListToJSON().toString());

                    ImageView iv_qr = findViewById(R.id.iv_qr);

                    generateQRCode(checkoutInfo.toString(), iv_qr);

                }
                catch (Exception e) {

                }
            }
        });

    }

    private void generateQRCode(String info, ImageView iv) {
        int size;
        String content = info;

        size = info.length();
        if (size < 1)
            size = 1;
        else if (size > 1536)
            size = 1536;
        byte[] bContent = new byte[size];
        for (int b=0; b<size; b++) {
            bContent[b] = (byte)(b%256);
        }

        final String QRcodeContents = content;
        // convert in a separate thread to avoid possible ANR
        Thread t = new Thread(() -> {
            final Bitmap bitmap = encodeAsBitmap(QRcodeContents);
            runOnUiThread(()->iv.setImageBitmap(bitmap));
        });
        t.start();
    }

    Bitmap encodeAsBitmap(String str) {
        BitMatrix result;

        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, CH_SET);
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, DIMENSION, DIMENSION, hints);
        }
        catch (Exception exc) {

            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int colorPrimary = getResources().getColor(R.color.colorPrimary, null);
        int colorWhite = 16777215;
        int[] pixels = new int[w * h];
        for (int line = 0; line < h; line++) {
            int offset = line * w;
            for (int col = 0; col < w; col++) {
                pixels[offset + col] = result.get(col, line) ? colorPrimary : colorWhite;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }


}
