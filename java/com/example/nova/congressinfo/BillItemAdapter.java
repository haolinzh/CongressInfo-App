package com.example.nova.congressinfo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by NOVA on 16/11/20.
 */

public class BillItemAdapter extends ArrayAdapter {

    List<Bill> billItem;
    LayoutInflater billInflator;

    public BillItemAdapter(Context context, List<Bill> objects) {

        super(context, R.layout.billlist_layout, objects);
        billItem=objects;

        billInflator=LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView=billInflator.inflate(R.layout.billlist_layout,parent,false);
        }

        TextView tvTitle=(TextView) convertView.findViewById(R.id.billId);
        TextView tvContent=(TextView) convertView.findViewById(R.id.billTitle);
        TextView tvDate=(TextView) convertView.findViewById(R.id.billDate);

        Bill bill=billItem.get(position);
        tvTitle.setText(bill.getBillId());
        tvContent.setText(bill.getBillTitle());
        tvDate.setText(bill.getBillDate());

        return convertView;
    }
}
