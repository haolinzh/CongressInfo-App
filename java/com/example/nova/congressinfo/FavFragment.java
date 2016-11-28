package com.example.nova.congressinfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import static com.example.nova.congressinfo.R.id.listViewFavBill;
import static com.example.nova.congressinfo.R.id.listViewFavComm;
import static com.example.nova.congressinfo.R.id.listViewFavLeg;


/**
 * Created by NOVA on 16/11/28.
 */

public class FavFragment extends Fragment implements TabHost.OnTabChangeListener {
    RelativeLayout layout;
    TabHost tabHost;
    ListView favLegView;
    ListView favBillView;
    ListView favCommView;

    public FavFragment(){

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

        return layout;
    }


    @Override
    public void onTabChanged(String s) {

    }
}
