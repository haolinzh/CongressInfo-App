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
public class BillFragment extends Fragment implements TabHost.OnTabChangeListener {
    RelativeLayout layout;
    TabHost tabHost;
    ListView activeBillView;
    ListView newBillView;
    List<Bill>  activeBillList;
    List<Bill>  newBillList;


    public BillFragment() {
        activeBillList = new ArrayList<>();
        newBillList=new ArrayList<>();

        ActiveTask actask = new ActiveTask();
        actask.execute("http://104.198.0.197:8080/bills?apikey=3e10ee5ae4ca4e5f884cbedf3ef2372a&per_page=50&history.active=true");

        NewTask nwtask=new NewTask();
        nwtask.execute("http://104.198.0.197:8080/bills?apikey=3e10ee5ae4ca4e5f884cbedf3ef2372a&per_page=50&history.active=false");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_bill, container, false);
        tabHost = (TabHost) layout.findViewById(android.R.id.tabhost);
        tabHost.setup();


        activeBillView = (ListView) layout.findViewById(R.id.listViewAcBill);
        newBillView = (ListView) layout.findViewById(R.id.listViewNewBill);

        activeBillView.setOnItemClickListener(onItemClickListener);
        newBillView.setOnItemClickListener(onItemClickListener);


        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("ACTIVE BILLS").setContent(R.id.tabActive));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("NEW BILLS").setContent(R.id.tabNew));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
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


    private class ActiveTask extends AsyncTask<Object, Object, List<Bill>> {


        @Override
        protected List<Bill> doInBackground(Object... params) {

            Object url=params[0];
            URLConnection connection;
            InputStream is;

            try {
                connection=new URL((String) url).openConnection();
                is=connection.getInputStream();
                BufferedReader bis=new BufferedReader(new InputStreamReader(is));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bis.readLine()) != null) {
                    response.append(line);
                }

                String finalJson=response.toString();
                JSONObject billJson=new JSONObject(finalJson);
                JSONArray billRes=billJson.getJSONArray("results");

                int len=billRes.length();
                for(int i=0;i<len;i++){

                    JSONObject singleBill=billRes.getJSONObject(i);

                    String billId=singleBill.getString("bill_id").toUpperCase();
                    String billTitle=singleBill.getString("official_title");
                    String billDate=singleBill.getString("introduced_on");
                    Bill b=new Bill(billId,billTitle,billDate);
                    activeBillList.add(b);

                }
                return activeBillList;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bill> bills) {
            BillItemAdapter adapter = new BillItemAdapter(getActivity().getApplicationContext(), activeBillList);
            activeBillView.setAdapter(adapter);

        }
    }



    private class NewTask extends AsyncTask<Object, Object, List<Bill>> {

        @Override
        protected List<Bill> doInBackground(Object... params) {

            Object url=params[0];
            URLConnection connection;
            InputStream is;

            try {
                connection=new URL((String) url).openConnection();
                is=connection.getInputStream();
                BufferedReader bis=new BufferedReader(new InputStreamReader(is));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bis.readLine()) != null) {
                    response.append(line);
                }

                String finalJson=response.toString();
                JSONObject billJson=new JSONObject(finalJson);
                JSONArray billRes=billJson.getJSONArray("results");

                int len=billRes.length();
                for(int i=0;i<len;i++){

                    JSONObject singleBill=billRes.getJSONObject(i);

                    String billId=singleBill.getString("bill_id").toUpperCase();
                    String billTitle=singleBill.getString("official_title");
                    String billDate=singleBill.getString("introduced_on");
                    Bill b=new Bill(billId,billTitle,billDate);
                    newBillList.add(b);

                }
                return newBillList;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bill> bills) {
            BillItemAdapter adapter = new BillItemAdapter(getActivity().getApplicationContext(), newBillList);
            newBillView.setAdapter(adapter);

        }
    }


}
