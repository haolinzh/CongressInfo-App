package com.example.nova.congressinfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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


    public class commHouseTask extends AsyncTask<String, Void, List<Comm>>{

        @Override
        protected List<Comm> doInBackground(String... params) {

            String url=params[0];
            URLConnection connection;
            InputStream is=null;
            BufferedReader bis=null;

            try {
                connection=new URL(url).openConnection();
                is=connection.getInputStream();
                bis=new BufferedReader(new InputStreamReader(is));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bis.readLine()) != null) {
                    response.append(line);
                }

                String finalJson=response.toString();
                JSONObject commJson=new JSONObject(finalJson);
                JSONArray commRes=commJson.getJSONArray("results");

                int len=commRes.length();
                for(int i=0;i<len;i++){

                    JSONObject singleComm=commRes.getJSONObject(i);

                    String commId=singleComm.getString("committee_id");
                    String commName=singleComm.getString("name");
                    String commChamber=singleComm.getString("chamber");



                    Comm c=new Comm(commId,commName,commChamber);
                    houseCommList.add(c);

                }
                return houseCommList;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                try {
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }
    }
}

