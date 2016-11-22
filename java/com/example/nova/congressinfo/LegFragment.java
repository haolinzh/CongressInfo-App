package com.example.nova.congressinfo;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * A simple {@link Fragment} subclass.
 */
public class LegFragment extends Fragment implements TabHost.OnTabChangeListener{
    RelativeLayout layout;
    TabHost tabHost;
    ListView legByStateView;
    List<Leg> legList;

    ListView legByHouseView;
    List<Leg> legHouseList;

    ListView legBySenateView;
    List<Leg> legSenateList;



    public LegFragment() {
        legList=new ArrayList<>();
        legHouseList=new ArrayList<>();
        legSenateList=new ArrayList<>();

        LegTask legtask=new LegTask();
        legtask.execute("http://104.198.0.197:8080/legislators?apikey=4acd972a599843bd93ea4dba171a483f&per_page=all");

        LegHouseTask legHouseTask=new LegHouseTask();
        legHouseTask.execute("http://104.198.0.197:8080/legislators?chamber=house&apikey=4acd972a599843bd93ea4dba171a483f&per_page=all");

        LegSenateTask legSenateTask=new LegSenateTask();
        legSenateTask.execute("http://104.198.0.197:8080/legislators?chamber=senate&apikey=4acd972a599843bd93ea4dba171a483f&per_page=all");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_leg, container, false);
        tabHost = (TabHost) layout.findViewById(android.R.id.tabhost);
        tabHost.setup();

        legByStateView=(ListView) layout.findViewById(R.id.legByStateView);
        legByHouseView=(ListView) layout.findViewById(R.id.legByHouseView);
        legBySenateView=(ListView) layout.findViewById(R.id.legBySenateView);

        legByStateView.setOnItemClickListener(onItemClickListener);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("BY STATES").setContent(R.id.legByState));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("HOUSE").setContent(R.id.legInHouse));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("SENATE").setContent(R.id.legInSenate));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);

        return layout;
    }

    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent=new Intent(getActivity().getApplicationContext(),LegDetail.class);
             Leg leg =(Leg) adapterView.getAdapter().getItem(i);
             intent.putExtra("legId",leg.getId());
            startActivity(intent);

        }
    };

    @Override
    public void onTabChanged(String s) {

    }


    public class LegTask extends AsyncTask<Object,Object,List<Leg>>{

        @Override
        protected List<Leg> doInBackground(Object... params) {

            Object url=params[0];
            URLConnection connection;
            InputStream is;
            try {
                connection = new URL((String) url).openConnection();
                is = connection.getInputStream();
                BufferedReader bis = new BufferedReader(new InputStreamReader(is));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bis.readLine()) != null) {
                    response.append(line);
                }

                String finalJson = response.toString();
                JSONObject legJson = new JSONObject(finalJson);
                JSONArray legRes = legJson.getJSONArray("results");

                int len = legRes.length();
                for (int i = 0; i < len; i++) {

                    JSONObject singleLeg = legRes.getJSONObject(i);

                    String legFirstName = singleLeg.getString("first_name");
                    String legLastName = singleLeg.getString("last_name");
                    String legName=legFirstName+", "+legLastName;

                    String legParty = singleLeg.getString("party");
                    String legState = singleLeg.getString("state_name");
                    String legDistrict=singleLeg.getString("district");
                    String id=singleLeg.getString("bioguide_id");

                    Leg l = new Leg(legName, legParty, legState,legDistrict,id);
                    legList.add(l);

                }
                return legList;
            }
            catch (IOException e){
                e.printStackTrace();

            }catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(List<Leg> legs) {
            LegItemAdapter legItemAdapter=new LegItemAdapter(getActivity().getApplicationContext(),legList);
            legByStateView.setAdapter(legItemAdapter);

        }
    }


    public class LegHouseTask extends AsyncTask<Object,Object,List<Leg>>{

        @Override
        protected List<Leg> doInBackground(Object... params) {

            Object url=params[0];
            URLConnection connection;
            InputStream is;
            try {
                connection = new URL((String) url).openConnection();
                is = connection.getInputStream();
                BufferedReader bis = new BufferedReader(new InputStreamReader(is));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bis.readLine()) != null) {
                    response.append(line);
                }

                String finalJson = response.toString();
                JSONObject legJson = new JSONObject(finalJson);
                JSONArray legRes = legJson.getJSONArray("results");

                int len = legRes.length();
                for (int i = 0; i < len; i++) {

                    JSONObject singleLeg = legRes.getJSONObject(i);

                    String legFirstName = singleLeg.getString("first_name");
                    String legLastName = singleLeg.getString("last_name");
                    String legName=legFirstName+", "+legLastName;

                    String legParty = singleLeg.getString("party");
                    String legState = singleLeg.getString("state_name");
                    String legDistrict=singleLeg.getString("district");
                    String id=singleLeg.getString("bioguide_id");

                    Leg l = new Leg(legName, legParty, legState,legDistrict,id);
                    legHouseList.add(l);

                }
                return legHouseList;
            }
            catch (IOException e){
                e.printStackTrace();

            }catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(List<Leg> legs) {
            LegItemAdapter legItemAdapter=new LegItemAdapter(getActivity().getApplicationContext(),legHouseList);
            legByHouseView.setAdapter(legItemAdapter);

        }
    }

    public class LegSenateTask extends AsyncTask<Object,Object,List<Leg>>{

        @Override
        protected List<Leg> doInBackground(Object... params) {

            Object url=params[0];
            URLConnection connection;
            InputStream is;
            try {
                connection = new URL((String) url).openConnection();
                is = connection.getInputStream();
                BufferedReader bis = new BufferedReader(new InputStreamReader(is));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bis.readLine()) != null) {
                    response.append(line);
                }

                String finalJson = response.toString();
                JSONObject legJson = new JSONObject(finalJson);
                JSONArray legRes = legJson.getJSONArray("results");

                int len = legRes.length();
                for (int i = 0; i < len; i++) {

                    JSONObject singleLeg = legRes.getJSONObject(i);

                    String legFirstName = singleLeg.getString("first_name");
                    String legLastName = singleLeg.getString("last_name");
                    String legName=legFirstName+", "+legLastName;

                    String legParty = singleLeg.getString("party");
                    String legState = singleLeg.getString("state_name");
                    String legDistrict=singleLeg.getString("district");
                    String id=singleLeg.getString("bioguide_id");

                    Leg l = new Leg(legName, legParty, legState,legDistrict,id);
                    legSenateList.add(l);

                }
                return legSenateList;
            }
            catch (IOException e){
                e.printStackTrace();

            }catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(List<Leg> legs) {
            LegItemAdapter legItemAdapter=new LegItemAdapter(getActivity().getApplicationContext(),legSenateList);
            legBySenateView.setAdapter(legItemAdapter);

        }
    }

}
