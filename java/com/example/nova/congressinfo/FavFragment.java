package com.example.nova.congressinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.example.nova.congressinfo.MainActivity.favLeg;
import static com.example.nova.congressinfo.R.id.listViewFavBill;
import static com.example.nova.congressinfo.R.id.listViewFavComm;
import static com.example.nova.congressinfo.R.id.listViewFavLeg;


/**
 * Created by NOVA on 16/11/28.
 */

public class FavFragment extends Fragment implements TabHost.OnTabChangeListener, View.OnClickListener {
    RelativeLayout layout;
    TabHost tabHost;

    List<Bill> favBillList;
    List<Comm> favCommList;
    List<Leg> favLegList;
    ListView favLegView;
    ListView favBillView;
    ListView favCommView;
 //   ListView favLegIndex;
    BillItemAdapter badapter;
    CommItemAdapter cadapter;
    LegItemAdapter ladapter;
    Gson gson;
    Map<String,Integer> legMap;
 //   LinearLayout sideLayout;


    public FavFragment(){
        favBillList=new ArrayList<>();
        favCommList=new ArrayList<>();
        favLegList=new ArrayList<>();
        gson=new Gson();
    }

    @Override
    public void onResume() {
        super.onResume();

        badapter.clear();
        badapter.notifyDataSetChanged();
        cadapter.clear();
        cadapter.notifyDataSetChanged();
        ladapter.clear();
        ladapter.notifyDataSetChanged();

        Iterator<String> billitr = MainActivity.favBill.iterator();
        while(billitr.hasNext()){
            String str = billitr.next();
            Bill b = gson.fromJson(str, Bill.class);
            favBillList.add(b);
        }

        Collections.sort(favBillList,billCmp);
        badapter = new BillItemAdapter(getActivity().getApplicationContext(), favBillList);
        favBillView.setAdapter(badapter);
        favBillView.setOnItemClickListener(onItemClickListenerb);



        Iterator<String> commitr = MainActivity.favComm.iterator();
        while(commitr.hasNext()){
            String str = commitr.next();
            Comm c = gson.fromJson(str, Comm.class);
            favCommList.add(c);
        }
        Collections.sort(favCommList,commCmp);
        cadapter = new CommItemAdapter(getActivity().getApplicationContext(), favCommList);
        favCommView.setAdapter(cadapter);
        favCommView.setOnItemClickListener(onItemClickListenerc);



        Iterator<String> legitr = favLeg.iterator();
        while(legitr.hasNext()){
            String str = legitr.next();
            Leg l = gson.fromJson(str, Leg.class);
            favLegList.add(l);
        }
        Collections.sort(favLegList,legCmp);
        ladapter = new LegItemAdapter(getActivity().getApplicationContext(), favLegList);
        favLegView.setAdapter(ladapter);
        favLegView.setOnItemClickListener(onItemClickListenerl);

        getLegIndex(favLegList);
        LinearLayout sideLayout = (LinearLayout)getActivity().findViewById(R.id.favlegIndexLayout);
        sideBarDisplay(sideLayout);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_fav, container, false);
        tabHost = (TabHost) layout.findViewById(android.R.id.tabhost);
        tabHost.setup();

        favLegView = (ListView) layout.findViewById(listViewFavLeg);
        favBillView = (ListView) layout.findViewById(listViewFavBill);
        favCommView = (ListView) layout.findViewById(listViewFavComm);



        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("LEGISLATORS").setContent(R.id.tabFavLeg));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("BILLS").setContent(R.id.tabFavBill));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("COMMITTEES").setContent(R.id.tabFavComm));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);


        Iterator<String> billitr = MainActivity.favBill.iterator();
        while(billitr.hasNext()){
            String str = billitr.next();
            Bill b = gson.fromJson(str, Bill.class);
            favBillList.add(b);
        }

        Collections.sort(favBillList,billCmp);
        badapter = new BillItemAdapter(getActivity().getApplicationContext(), favBillList);
        favBillView.setAdapter(badapter);
        favBillView.setOnItemClickListener(onItemClickListenerb);


        Iterator<String> commitr = MainActivity.favComm.iterator();
        while(commitr.hasNext()){
            String str = commitr.next();
            Comm c = gson.fromJson(str, Comm.class);
            favCommList.add(c);
        }

        Collections.sort(favCommList,commCmp);
        cadapter = new CommItemAdapter(getActivity().getApplicationContext(), favCommList);
        favCommView.setAdapter(cadapter);
        favCommView.setOnItemClickListener(onItemClickListenerc);


        Iterator<String> legitr = favLeg.iterator();
        while(legitr.hasNext()){
            String str = legitr.next();
            Leg l = gson.fromJson(str, Leg.class);
            favLegList.add(l);
        }
        Collections.sort(favLegList,legCmp);
        ladapter = new LegItemAdapter(getActivity().getApplicationContext(), favLegList);
        favLegView.setAdapter(ladapter);
        favLegView.setOnItemClickListener(onItemClickListenerl);



        getLegIndex(favLegList);
        LinearLayout sideLayout = (LinearLayout) layout.findViewById(R.id.favlegIndexLayout);
        sideBarDisplay(sideLayout);


        return layout;
    }


    private AdapterView.OnItemClickListener onItemClickListenerb=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent=new Intent(getActivity().getApplicationContext(),BillDetail.class);
            Bill bill =(Bill) adapterView.getAdapter().getItem(i);
            intent.putExtra("billId",bill.getBillId());
            startActivity(intent);

        }
    };


    private AdapterView.OnItemClickListener onItemClickListenerc=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent=new Intent(getActivity().getApplicationContext(),CommDetail.class);
            Comm comm =(Comm) adapterView.getAdapter().getItem(i);
            intent.putExtra("comm",comm);
            startActivity(intent);

        }
    };


    private AdapterView.OnItemClickListener onItemClickListenerl=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent=new Intent(getActivity().getApplicationContext(),LegDetail.class);
            Leg leg =(Leg) adapterView.getAdapter().getItem(i);
            intent.putExtra("legId",leg.getId());
            startActivity(intent);

        }
    };


    private void getLegIndex(List<Leg> list) {
        legMap = new TreeMap<>();
        for (int i = 0; i < list.size(); i++) {
            Leg leg = list.get(i);
            String index = leg.getName().substring(0, 1);
            if (!legMap.containsKey(index)) {
                legMap.put(index, i);
            }
        }

    }


    private void sideBarDisplay(LinearLayout layout) {
        layout.removeAllViews();
        TextView tv;
        List<String> sideIndex = new ArrayList<>(legMap.keySet());
        for (String index : sideIndex) {
            tv = (TextView) getActivity().getLayoutInflater().inflate(R.layout.sidebar_index, null);
            tv.setText(index);
            tv.setOnClickListener(this);
            layout.addView(tv);
        }

    }

    @Override
    public void onClick(View view) {
        TextView tmpTv = (TextView) view;
        favLegView.setSelection(legMap.get(tmpTv.getText()));
    }






    @Override
    public void onTabChanged(String s) {

    }

    Comparator<Bill> billCmp=new Comparator<Bill>() {
        @Override
        public int compare(Bill b1, Bill b2) {
            SimpleDateFormat sdf=new SimpleDateFormat("MMM dd,yyyy");
            try {
                Date d1=sdf.parse(b1.getBillDate());
                Date d2=sdf.parse(b2.getBillDate());
                return  d2.compareTo(d1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;

        }
    };


    Comparator<Comm> commCmp=new Comparator<Comm>() {
        @Override
        public int compare(Comm c1, Comm c2) {
            return c1.getCommName().compareTo(c2.getCommName());
        }
    };

    Comparator<Leg> legCmp = new Comparator<Leg>() {
        @Override
        public int compare(Leg l1, Leg l2) {
            return l1.getName().compareTo(l2.getName());
        }
    };



}
