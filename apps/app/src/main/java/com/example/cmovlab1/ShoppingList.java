package com.example.cmovlab1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ShoppingList extends AppCompatActivity {

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    static ArrayList<Item> items = new ArrayList<>();
    ItemListAdapter adapter;
    Item currItem = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        bindHandlers();
        setupList();
    }

    private void bindHandlers() {
        scanNewItemHandler();
        addToListHandler();
    }

    private void setupList() {
        adapter = new ItemListAdapter(this, android.R.layout.simple_list_item_1, items);
        ListView list_items = findViewById(R.id.lv_items);
        list_items.setAdapter(adapter);
    }

    private void addToListHandler() {
        Button btn_addcart = (Button) findViewById(R.id.btn_addcart);
        btn_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currItem != null && items.size() < 10)
                    adapter.add(currItem);
            }
        });
    }

    private void scanNewItemHandler() {
        Button btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
    }

    public void scan() {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        }
        catch (ActivityNotFoundException anfe) {
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final AppCompatActivity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, (dialogInterface, i) -> {
            Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            act.startActivity(intent);
        });
        downloadDialog.setNegativeButton(buttonNo, null);
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");

                try {
                    JSONObject itemJson = new JSONObject(contents);
                    String id = itemJson.getString("id");
                    String name = itemJson.getString("name");
                    int cents = Integer.parseInt(itemJson.getString("cents"));

                    currItem = new Item(id, name, cents);

                    ((TextView) findViewById(R.id.tv_prodname)).setText(currItem.getName());
                    ((TextView) findViewById(R.id.tv_item_price)).setText(currItem.getEuros() + "." + currItem.getCents() + " €");

                }
                catch (Exception e) {

                }

            }
        }
    }

}
