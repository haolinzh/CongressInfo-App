package com.example.nova.congressinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by NOVA on 16/11/21.
 */

public class LegItemAdapter extends ArrayAdapter {
    List<Leg> legItem;
    LayoutInflater legInflator;

    public LegItemAdapter(Context context, List<Leg> objects) {
        super(context, R.layout.leglist_layout, objects);

        legItem=objects;
        legInflator=LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView=legInflator.inflate(R.layout.leglist_layout,parent,false);
        }

        TextView tvInfo1=(TextView) convertView.findViewById(R.id.legInfo1);
        TextView tvInfo2=(TextView) convertView.findViewById(R.id.legInfo2);
        ImageView imgLeg= (ImageView) convertView.findViewById(R.id.legImage);

        Leg leg=legItem.get(position);


        tvInfo1.setText(leg.getName());
        tvInfo2.setText("("+leg.getParty()+")"+leg.getState()+" - District "+leg.getDistrict());
        String picUrl="https://theunitedstates.io/images/congress/original/"+leg.getId()+".jpg";
        Picasso.with(getContext()).load(picUrl).resize(67,83).into(imgLeg);

        return convertView;
    }


//    public View getView(int position, View convertView, ViewGroup parent){
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = legInflator.inflate(R.layout.leglist_layout, parent, false);
//            holder = new ViewHolder();
//            holder.info1 = (TextView) convertView.findViewById(R.id.legInfo1);
//            holder.info2 = (TextView) convertView.findViewById(R.id.legInfo1);
//            holder.imgv=(ImageView) convertView.findViewById(R.id.legImage);
//
//            convertView.setTag(holder);
//        }
//        else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        Leg leg=legItem.get(position);
//
//        holder.info1.setText(leg.getName());
//        holder.info2.setText("("+leg.getParty()+")"+leg.getState()+" - District "+leg.getDistrict());
//        String picUrl="https://theunitedstates.io/images/congress/original/"+leg.getId()+".jpg";
//        Picasso.with(getContext()).load(picUrl).resize(40,40).into(holder.imgv);
//        return convertView;
//
//    }
//
//
//    class ViewHolder {
//        public TextView info1;
//        public TextView info2;
//        public ImageView imgv;
//    }
}
