package com.example.cmovlab1;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class VoucherListAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> vouchers;
    private int resourceId;

    public VoucherListAdapter(@NonNull Context context, int resourceId, ArrayList<String> vouchers) {
        super(context, resourceId, vouchers);
        mContext = context;
        this.vouchers = vouchers;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.voucherlist_item, null);
        }

        String v = vouchers.get(position);
        ((TextView) row.findViewById(R.id.tv_voucher_id)).setText(v);

        return row;
    }
}
