package com.example.cmovlab1;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cmovlab1.R;

import java.util.ArrayList;

public class ItemListAdapter extends ArrayAdapter<Item> {

    private Context mContext;
    private ArrayList<Item> items;
    private int resourceId;

    public ItemListAdapter(@NonNull Context context, int resourceId, ArrayList<Item> items) {
        super(context, resourceId, items);
        mContext = context;
        this.items = items;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.shoppinglist_item, null);
        }

        Item i = items.get(position);
        ((TextView) row.findViewById(R.id.tv_prod_name_row)).setText(i.getName());
        ((TextView) row.findViewById(R.id.tv_price_row)).setText(i.getEuros() + "." + i.getCents() + " €");

        ImageButton btn_removeitem = row.findViewById(R.id.btn_removeitem);
        btn_removeitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

        return row;
    }

    private void removeItem(int index) {
        this.remove(this.getItem(index));
    }

}
