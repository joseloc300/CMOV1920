package com.example.terminal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CheckoutActivity extends AppCompatActivity {

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Checkout");
        setContentView(R.layout.activity_checkout);
        bindHandlers();
    }

    private void bindHandlers() {
        checkoutButtonHandler();
    }

    private void checkoutButtonHandler() {
        Button btn_scan = (Button)findViewById(R.id.btn_scan);
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
                    JSONObject checkoutInfo = new JSONObject(contents);

                    String user = checkoutInfo.getString("user_uuid");
                    String items = checkoutInfo.getString("items");
                    String voucher = checkoutInfo.getString("voucher");
                    String useDiscount = checkoutInfo.getString("useDiscount");

                    sendCheckoutRequest(user, items, voucher, useDiscount);
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error reading the checkout QR code.", Toast.LENGTH_SHORT);
                }



            }
        }
    }

    private void sendCheckoutRequest(String user, String items, String voucher, String useDiscount) {
        String ip = getSharedPreferences("server", MODE_PRIVATE).getString("server_ip", "");
        RequestParams rq = new RequestParams();
        rq.add("user_uuid", user);
        rq.add("items", items);
        rq.add("voucher", voucher);
        rq.add("useDiscount", useDiscount);
        ServerRestClient.post(ip, "/checkout", rq, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    TextView tv_status = findViewById(R.id.tv_status);
                    tv_status.setText("Status: " + response.getString("status"));

                    TextView tv_spent = findViewById(R.id.tv_spent);
                    int spent = Integer.parseInt(response.getString("spent"));
                    int euros = spent / 100;
                    int cents = spent % 100;
                    tv_spent.setText("Total Spent: " + euros + "." + cents + "€.");
                }
                catch (Exception e) {
                    TextView tv_status = findViewById(R.id.tv_status);
                    tv_status.setText("Status: Failure");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                TextView tv_status = findViewById(R.id.tv_status);
                tv_status.setText("Failure");
            }
        });
    }
}
