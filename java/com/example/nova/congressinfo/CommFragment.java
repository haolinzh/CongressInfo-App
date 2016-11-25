package com.example.nova.congressinfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NOVA on 16/11/24.
 */

public class CommFragment extends Fragment implements  TabHost.OnTabChangeListener{
    RelativeLayout layout;
    TabHost tabHost;
    ListView houseCommView;
    ListView senateCommView;
    ListView jointCommView;
    List<Comm> houseCommList;
    List<Comm> senateCommList;
    List<Comm> jointCommList;


    public CommFragment(){
        houseCommList=new ArrayList<>();
        senateCommList=new ArrayList<>();
        jointCommList=new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_comm, container, false);
        tabHost = (TabHost) layout.findViewById(android.R.id.tabhost);
        tabHost.setup();

        houseCommView= (ListView) layout.findViewById(R.id.listViewHouseComm);
        senateCommView= (ListView) layout.findViewById(R.id.listViewSenateComm);
        jointCommView= (ListView) layout.findViewById(R.id.listViewJointComm);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("HOUSE").setContent(R.id.tabHouseComm));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("SENATE").setContent(R.id.tabSenateComm));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("JOINT").setContent(R.id.tabJointComm));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
        return layout;
    }

    @Override
    public void onTabChanged(String s) {

    }
}
