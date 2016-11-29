package com.example.nova.congressinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static com.example.nova.congressinfo.R.id.listViewFavBill;
import static com.example.nova.congressinfo.R.id.listViewFavComm;
import static com.example.nova.congressinfo.R.id.listViewFavLeg;


/**
 * Created by NOVA on 16/11/28.
 */

public class FavFragment extends Fragment implements TabHost.OnTabChangeListener {
    RelativeLayout layout;
    TabHost tabHost;
    List<Bill> favBillList;
    ListView favLegView;
    ListView favBillView;
    ListView favCommView;
    SharedPreferences sharedPref;

    public FavFragment(){
        favBillList=new ArrayList<>();

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

        sharedPref=getActivity().getSharedPreferences("FavSp",MODE_PRIVATE);
        Gson gson=new Gson();

        Set<String> favBillJson= sharedPref.getStringSet("favBillJson",new HashSet<String>());

        Iterator<String> itr = favBillJson.iterator();

        while(itr.hasNext()){
            String str = itr.next();
            Bill b = gson.fromJson(str, Bill.class);
            favBillList.add(b);
        }

        BillItemAdapter adapter = new BillItemAdapter(getActivity().getApplicationContext(), favBillList);
        favBillView.setAdapter(adapter);
        favBillView.setOnItemClickListener(onItemClickListener);

        return layout;
    }


    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent=new Intent(getActivity().getApplicationContext(),BillDetail.class);
            Bill bill =(Bill) adapterView.getAdapter().getItem(i);
            intent.putExtra("billId",bill.getBillId());
            startActivity(intent);

        }
    };


    @Override
    public void onTabChanged(String s) {

    }
}
