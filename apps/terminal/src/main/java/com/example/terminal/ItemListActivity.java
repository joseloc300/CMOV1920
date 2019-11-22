package com.example.terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.util.Base64;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import cz.msebera.android.httpclient.Header;

public class ItemListActivity extends AppCompatActivity {

    private ArrayList<Item> items = new ArrayList<>();
    private final int DIMENSION = 500;
    private final static String CH_SET="ISO-8859-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Item List");
        setContentView(R.layout.activity_item_list);
        retrieveItems();
    }

    private void retrieveItems() {
        String ip = getSharedPreferences("server", MODE_PRIVATE).getString("server_ip", "");
        ServerRestClient.get(ip, "/items", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(statusCode == 200) {
                    Log.d("items", response.toString());

                    Iterator<String> keys = response.keys();

                    while(keys.hasNext()) {
                        String key = keys.next();
                        try {
                            String item = response.getString(key);
                            item = item.substring(1);

                            byte[] encodedBytes = Base64.decode(item, Base64.DEFAULT);
                            String decoded = new String(encodedBytes);
                            JSONObject itemjson = new JSONObject(decoded);

                            String name = itemjson.getString("name");
                            int cents = itemjson.getInt("cents");

                            Item i = new Item(key, name, cents, decoded);
                            items.add(i);

                        }
                        catch (Exception e) {
                            Log.d("exception", e.getMessage());
                        }

                    }

                    generateItemCodes();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response){
                Toast.makeText(getApplicationContext(),"Failed to retrieve items from the server.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateItemCodes() {
        ImageView iv_1 = findViewById(R.id.iv_1);
        ImageView iv_2 = findViewById(R.id.iv_2);
        ImageView iv_3 = findViewById(R.id.iv_3);
        ImageView iv_4 = findViewById(R.id.iv_4);
        ImageView iv_5 = findViewById(R.id.iv_5);
        ImageView iv_6 = findViewById(R.id.iv_6);

        TextView tv_name_1 = findViewById(R.id.tv_name_1);
        TextView tv_name_2 = findViewById(R.id.tv_name_2);
        TextView tv_name_3 = findViewById(R.id.tv_name_3);
        TextView tv_name_4 = findViewById(R.id.tv_name_4);
        TextView tv_name_5 = findViewById(R.id.tv_name_5);
        TextView tv_name_6 = findViewById(R.id.tv_name_6);

        TextView tv_price_1 = findViewById(R.id.tv_price_1);
        TextView tv_price_2 = findViewById(R.id.tv_price_2);
        TextView tv_price_3 = findViewById(R.id.tv_price_3);
        TextView tv_price_4 = findViewById(R.id.tv_price_4);
        TextView tv_price_5 = findViewById(R.id.tv_price_5);
        TextView tv_price_6 = findViewById(R.id.tv_price_6);

        tv_name_1.setText(items.get(0).getName());
        tv_price_1.setText(items.get(0).getEuros() + "." + items.get(0).getCents() + " €");
        generateQRCode(items.get(0).getInfo(), iv_1);

        tv_name_2.setText(items.get(1).getName());
        tv_price_2.setText(items.get(1).getEuros() + "." + items.get(1).getCents() + " €");
        generateQRCode(items.get(1).getInfo(), iv_2);

        tv_name_3.setText(items.get(2).getName());
        tv_price_3.setText(items.get(2).getEuros() + "." + items.get(2).getCents() + " €");
        generateQRCode(items.get(2).getInfo(), iv_3);

        tv_name_4.setText(items.get(3).getName());
        tv_price_4.setText(items.get(3).getEuros() + "." + items.get(3).getCents() + " €");
        generateQRCode(items.get(3).getInfo(), iv_4);

        tv_name_5.setText(items.get(4).getName());
        tv_price_5.setText(items.get(4).getEuros() + "." + items.get(4).getCents() + " €");
        generateQRCode(items.get(4).getInfo(), iv_5);

        tv_name_6.setText(items.get(5).getName());
        tv_price_6.setText(items.get(5).getEuros() + "." + items.get(5).getCents() + " €");
        generateQRCode(items.get(5).getInfo(), iv_6);

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
