package com.example.cmovlab1;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {
    private Context mContext;
    private ArrayList<Transaction> transactions;
    private int resourceId;

    public TransactionListAdapter(@NonNull Context context, int resourceId, ArrayList<Transaction> transactions) {
        super(context, resourceId, transactions);
        mContext = context;
        this.transactions = transactions;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.transactionlist_item, null);
        }

        Transaction v = transactions.get(position);
        ((TextView) row.findViewById(R.id.tv_transaction_total)).setText("Total: " + v.getTotalEuro() + "." + v.getTotalCents() + "€");
        ((TextView) row.findViewById(R.id.tv_transaction_voucher)).setText("Used Voucher: " + v.getUsedVoucher());
        ((TextView) row.findViewById(R.id.tv_transaction_discount)).setText("Used Discount: " + v.getUsedDiscount());
        ((TextView) row.findViewById(R.id.tv_transaction_items)).setText("Items:" + v.getItems());

        return row;
    }
}
