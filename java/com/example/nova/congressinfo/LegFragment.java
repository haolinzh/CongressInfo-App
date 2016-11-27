package com.example.nova.congressinfo;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class LegFragment extends Fragment implements TabHost.OnTabChangeListener, View.OnClickListener {
    RelativeLayout layout;
    TabHost tabHost;
    ListView legByStateView;
    List<Leg> legList;
    ListView legByHouseView;
    List<Leg> legHouseList;

    ListView legBySenateView;
    List<Leg> legSenateList;

    Map<String, Integer> legStateMap;
    Map<String, Integer> legHouseMap;
    Map<String, Integer> legSenateMap;


    public LegFragment() {
        legList = new ArrayList<>();
        legHouseList = new ArrayList<>();
        legSenateList = new ArrayList<>();

        LegTask legtask = new LegTask();
        legtask.execute("http://104.198.0.197:8080/legislators?apikey=4acd972a599843bd93ea4dba171a483f&per_page=all");

        LegHouseTask legHouseTask = new LegHouseTask();
        legHouseTask.execute("http://104.198.0.197:8080/legislators?chamber=house&apikey=4acd972a599843bd93ea4dba171a483f&per_page=all");

        LegSenateTask legSenateTask = new LegSenateTask();
        legSenateTask.execute("http://104.198.0.197:8080/legislators?chamber=senate&apikey=4acd972a599843bd93ea4dba171a483f&per_page=all");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_leg, container, false);
        tabHost = (TabHost) layout.findViewById(android.R.id.tabhost);
        tabHost.setup();

        legByStateView = (ListView) layout.findViewById(R.id.legByStateView);
        legByHouseView = (ListView) layout.findViewById(R.id.legByHouseView);
        legBySenateView = (ListView) layout.findViewById(R.id.legBySenateView);

        legByStateView.setOnItemClickListener(onItemClickListener);
        legByHouseView.setOnItemClickListener(onItemClickListener);
        legBySenateView.setOnItemClickListener(onItemClickListener);


        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("BY STATES").setContent(R.id.legByState));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("HOUSE").setContent(R.id.legInHouse));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("SENATE").setContent(R.id.legInSenate));


        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);

        return layout;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity().getApplicationContext(), LegDetail.class);
            Leg leg = (Leg) adapterView.getAdapter().getItem(i);
            intent.putExtra("legId", leg.getId());
            startActivity(intent);

        }
    };

    @Override
    public void onTabChanged(String s) {

    }


    public class LegTask extends AsyncTask<String, Void, List<Leg>> {

        @Override
        protected List<Leg> doInBackground(String... params) {

            String url = params[0];
            URLConnection connection;
            InputStream is=null;
            BufferedReader bis=null;
            try {
                connection = new URL(url).openConnection();
                is = connection.getInputStream();
                bis = new BufferedReader(new InputStreamReader(is));

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
                    String legName = legLastName + ", " + legFirstName;

                    String legParty = singleLeg.getString("party");
                    String legState = singleLeg.getString("state_name");
                    String legDistrict = singleLeg.getString("district");
                    String id = singleLeg.getString("bioguide_id");

                    if (legDistrict.equals("null")) {
                        legDistrict = "0";
                    }

                    Leg l = new Leg(legName, legParty, legState, legDistrict, id);
                    legList.add(l);

                }
                return legList;
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


        @Override
        protected void onPostExecute(List<Leg> legs) {
            Activity myActivity = getActivity();
            if (myActivity == null) return;

            Collections.sort(legList, cmpbyState);
            LegItemAdapter legItemAdapter = new LegItemAdapter(myActivity.getApplicationContext(), legList);
            legByStateView.setAdapter(legItemAdapter);

            getLegStateIndex(legList);
            LinearLayout sideLayout = (LinearLayout) myActivity.findViewById(R.id.legStateIndex);
            sideBarDisplay(sideLayout);

        }
    }

    public class LegHouseTask extends AsyncTask<String, Void, List<Leg>> {

        @Override
        protected List<Leg> doInBackground(String... params) {

            String url = params[0];
            URLConnection connection;
            InputStream is = null;
            BufferedReader bis = null;
            try {
                connection = new URL(url).openConnection();
                is = connection.getInputStream();
                bis = new BufferedReader(new InputStreamReader(is));

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
                    String legName = legLastName + ", " + legFirstName;

                    String legParty = singleLeg.getString("party");
                    String legState = singleLeg.getString("state_name");
                    String legDistrict = singleLeg.getString("district");
                    String id = singleLeg.getString("bioguide_id");


                    Leg l = new Leg(legName, legParty, legState, legDistrict, id);
                    legHouseList.add(l);

                }
                return legHouseList;
            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                try {
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(List<Leg> legs) {
            Activity myActivity = getActivity();
            if (myActivity == null) return;

            Collections.sort(legHouseList, cmpbyName);
            LegItemAdapter legItemAdapter = new LegItemAdapter(myActivity.getApplicationContext(), legHouseList);
            legByHouseView.setAdapter(legItemAdapter);

            getLegHouseIndex(legHouseList);
            LinearLayout sideLayout = (LinearLayout) myActivity.findViewById(R.id.legHouseIndex);
            sideBarDisplay(sideLayout);

        }
    }

    public class LegSenateTask extends AsyncTask<String, Void, List<Leg>> {

        @Override
        protected List<Leg> doInBackground(String... params) {

            String url = params[0];
            URLConnection connection;
            InputStream is=null;
            BufferedReader bis=null;
            try {
                connection = new URL(url).openConnection();
                is = connection.getInputStream();
                bis = new BufferedReader(new InputStreamReader(is));

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
                    String legName = legLastName + ", " + legFirstName;

                    String legParty = singleLeg.getString("party");
                    String legState = singleLeg.getString("state_name");
                    String legDistrict = singleLeg.getString("district");
                    String id = singleLeg.getString("bioguide_id");

                    if (legDistrict.equals("null")) {
                        legDistrict = "0";
                    }

                    Leg l = new Leg(legName, legParty, legState, legDistrict, id);
                    legSenateList.add(l);

                }
                return legSenateList;
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


        @Override
        protected void onPostExecute(List<Leg> legs) {
            Activity myActivity = getActivity();
            if (myActivity == null) return;

            Collections.sort(legSenateList, cmpbyName);
            LegItemAdapter legItemAdapter = new LegItemAdapter(myActivity.getApplicationContext(), legSenateList);
            legBySenateView.setAdapter(legItemAdapter);


            getLegSenateIndex(legSenateList);
            LinearLayout sideLayout = (LinearLayout) myActivity.findViewById(R.id.legSenateIndex);
            sideBarDisplay(sideLayout);


        }
    }


    Comparator<Leg> cmpbyState = new Comparator<Leg>() {
        @Override
        public int compare(Leg l1, Leg l2) {

            if (l1.getState().compareTo(l2.getState()) != 0) {
                return l1.getState().compareTo(l2.getState());
            } else {
                return l1.getName().compareTo(l2.getName());
            }

        }
    };

    Comparator<Leg> cmpbyName = new Comparator<Leg>() {
        @Override
        public int compare(Leg l1, Leg l2) {
            return l1.getName().compareTo(l2.getName());
        }
    };


    private void getLegStateIndex(List<Leg> list) {
        legStateMap = new TreeMap<>();
        for (int i = 0; i < list.size(); i++) {
            Leg leg = list.get(i);
            String index = leg.getState().substring(0, 1);
            if (!legStateMap.containsKey(index)) {
                legStateMap.put(index, i);
            }
        }

    }

    private void getLegHouseIndex(List<Leg> list) {
        legHouseMap = new TreeMap<>();
        for (int i = 0; i < list.size(); i++) {
            Leg leg = list.get(i);
            String index = leg.getName().substring(0, 1);
            if (!legHouseMap.containsKey(index)) {
                legHouseMap.put(index, i);
            }
        }

    }

    private void getLegSenateIndex(List<Leg> list) {
        legSenateMap = new TreeMap<>();
        for (int i = 0; i < list.size(); i++) {
            Leg leg = list.get(i);
            String index = leg.getName().substring(0, 1);
            if (!legSenateMap.containsKey(index)) {
                legSenateMap.put(index, i);
            }
        }

    }


    private void sideBarDisplay(LinearLayout layout) {
        TextView tv;
        Map<String, Integer> tmp;

        if (layout.getId() == R.id.legStateIndex) {
            tmp = legStateMap;
        } else if (layout.getId() == R.id.legHouseIndex) {
            tmp = legHouseMap;
        } else {
            tmp = legSenateMap;
        }

        List<String> sideIndex = new ArrayList<>(tmp.keySet());
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
        if (tabHost.getCurrentTab() == 0) {
            legByStateView.setSelection(legStateMap.get(tmpTv.getText()));
        } else if (tabHost.getCurrentTab() == 1) {
            legByHouseView.setSelection(legHouseMap.get(tmpTv.getText()));
        } else {
            legBySenateView.setSelection(legSenateMap.get(tmpTv.getText()));
        }
    }


}
