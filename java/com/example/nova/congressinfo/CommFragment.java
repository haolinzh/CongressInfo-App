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
import java.util.Collections;
import java.util.Comparator;
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


        CommHouseTask houseTask=new CommHouseTask();
        houseTask.execute("http://104.198.0.197:8080/committees?chamber=house&apikey=3e10ee5ae4ca4e5f884cbedf3ef2372a&per_page=all");

        CommSenateTask senateTask=new CommSenateTask();
        senateTask.execute("http://104.198.0.197:8080/committees?chamber=senate&apikey=3e10ee5ae4ca4e5f884cbedf3ef2372a&per_page=all");

        CommJointTask jointTask=new CommJointTask();
        jointTask.execute("http://104.198.0.197:8080/committees?chamber=joint&apikey=3e10ee5ae4ca4e5f884cbedf3ef2372a&per_page=all");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_comm, container, false);
        tabHost = (TabHost) layout.findViewById(android.R.id.tabhost);
        tabHost.setup();

        houseCommView= (ListView) layout.findViewById(R.id.listViewHouseComm);
        senateCommView= (ListView) layout.findViewById(R.id.listViewSenateComm);
        jointCommView= (ListView) layout.findViewById(R.id.listViewJointComm);


        houseCommView.setOnItemClickListener(onItemClickListener);
        senateCommView.setOnItemClickListener(onItemClickListener);
        jointCommView.setOnItemClickListener(onItemClickListener);


        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("HOUSE").setContent(R.id.tabHouseComm));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("SENATE").setContent(R.id.tabSenateComm));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("JOINT").setContent(R.id.tabJointComm));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
        return layout;
    }


    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent=new Intent(getActivity().getApplicationContext(),CommDetail.class);
            Comm comm =(Comm) adapterView.getAdapter().getItem(i);

            intent.putExtra("comm",comm);
            startActivity(intent);

        }
    };




    @Override
    public void onTabChanged(String s) {

    }


    public class CommHouseTask extends AsyncTask<String, Void, List<Comm>>{

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
                    String commPhone="N.A";
                    String commOffice="N.A";
                    String commPid="N.A";

                    if (singleComm.has("phone")){
                        commPhone=singleComm.getString("phone");
                    }
                    if (singleComm.has("office")){
                        commOffice=singleComm.getString("office");
                    }
                    if(singleComm.has("parent_committee_id"))    {
                        commPid=singleComm.getString("parent_committee_id");
                    }


                    Comm c=new Comm(commId,commName,commChamber,commPhone,commPid,commOffice);
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


        @Override
        protected void onPostExecute(List<Comm> comms) {
            Activity myActivity=getActivity();
            if (myActivity==null) return;

            Collections.sort(houseCommList,cmp);
            CommItemAdapter adapter = new CommItemAdapter(myActivity.getApplicationContext(), houseCommList);
            houseCommView.setAdapter(adapter);
        }
    }

    public class CommSenateTask extends AsyncTask<String, Void, List<Comm>>{

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


                    String commPhone="N.A";
                    String commPid="N.A";
                    String commOffice="N.A";

                    if (singleComm.has("phone")){
                        commPhone=singleComm.getString("phone");
                    }
                    if(singleComm.has("parent_committee_id")){
                        commPid=singleComm.getString("parent_committee_id");
                    }
                    if (singleComm.has("office")){
                        commOffice=singleComm.getString("office");
                    }

                    Comm c=new Comm(commId,commName,commChamber,commPhone,commPid,commOffice);

                    senateCommList.add(c);

                }
                return senateCommList;


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
        protected void onPostExecute(List<Comm> comms) {
            Activity myActivity=getActivity();
            if (myActivity==null) return;

            Collections.sort(senateCommList,cmp);
            CommItemAdapter adapter = new CommItemAdapter(myActivity.getApplicationContext(), senateCommList);
            senateCommView.setAdapter(adapter);
        }
    }

    public class CommJointTask extends AsyncTask<String, Void, List<Comm>>{

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

                    String commPhone="N.A";
                    String commPid="N.A";
                    String commOffice="N.A";

                    if (singleComm.has("phone")){
                        commPhone=singleComm.getString("phone");
                    }
                    if(singleComm.has("parent_committee_id")){
                        commPid=singleComm.getString("parent_committee_id");
                    }
                    if (singleComm.has("office")){
                        commOffice=singleComm.getString("office");
                    }


                    Comm c=new Comm(commId,commName,commChamber,commPhone,commPid,commOffice);
                    jointCommList.add(c);

                }
                return jointCommList;


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
        protected void onPostExecute(List<Comm> comms) {
            Activity myActivity=getActivity();
            if (myActivity==null) return;

              Collections.sort(jointCommList,cmp);
            CommItemAdapter adapter = new CommItemAdapter(myActivity.getApplicationContext(), jointCommList);
            jointCommView.setAdapter(adapter);
        }
    }


    Comparator<Comm> cmp=new Comparator<Comm>() {
        @Override
        public int compare(Comm c1, Comm c2) {
            return c1.getCommName().compareTo(c2.getCommName());
        }
    };

}

