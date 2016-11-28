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
 * Created by NOVA on 16/11/27.
 */

public class CommItemAdapter extends ArrayAdapter {

    List<Comm> commItem;
    LayoutInflater commInflator;

    public CommItemAdapter(Context context, List<Comm> objects) {

        super(context, R.layout.commlist_layout, objects);
        commItem=objects;

        commInflator=LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView=commInflator.inflate(R.layout.commlist_layout,parent,false);
        }

        TextView tvId=(TextView) convertView.findViewById(R.id.commId);
        TextView tvName=(TextView) convertView.findViewById(R.id.commName);
        TextView tvChamber=(TextView) convertView.findViewById(R.id.commChamber);

        Comm comm=commItem.get(position);
        tvId.setText(comm.getCommId());
        tvName.setText(comm.getCommName());
        tvChamber.setText(comm.getCommChamber());

        return convertView;
    }
}
