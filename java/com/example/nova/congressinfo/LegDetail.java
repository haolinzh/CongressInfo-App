package com.example.nova.congressinfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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
import java.util.List;

/**
 * Created by NOVA on 16/11/21.
 */

public class LegDetail extends AppCompatActivity {
    String id;
    List<String> legDetail;
    TextView tvName;
    TextView tvEmail;
    TextView tvChamber;
    TextView tvContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leg_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Legislator Info");
        id = getIntent().getStringExtra("legId");
        Log.d("txt",id);

        DetTask detTask=new DetTask();
        detTask.execute("http://104.198.0.197:8080/legislators?bioguide_id="+id+"&apikey=4acd972a599843bd93ea4dba171a483f");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            android.app.FragmentManager manager = getFragmentManager();
            manager.popBackStack();
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }


    private class DetTask extends AsyncTask<Object, Void, List<String>> {

        protected List<String> doInBackground(Object... params) {
            legDetail = new ArrayList<>();

            Object url = params[0];
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

                JSONObject singleLeg = legRes.getJSONObject(0);

//                String legParty = singleLeg.getString("party");

                String legFirstName = singleLeg.getString("first_name");
                String legLastName = singleLeg.getString("last_name");

                String legName = legFirstName + ", " + legLastName;
                String legEmail=singleLeg.getString("oc_email");
                String legChamber=singleLeg.getString("chamber");
                String legContact=singleLeg.getString("phone");

                legDetail.add(legName);
                legDetail.add(legEmail);
                legDetail.add(legChamber);
                legDetail.add(legContact);

                return legDetail;


            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;


        }


        @Override
        protected void onPostExecute(List<String> strings) {
            tvName= (TextView) findViewById(R.id.detName);
            tvEmail= (TextView) findViewById(R.id.detEmail);
            tvChamber= (TextView) findViewById(R.id.detChamber);
            tvContact= (TextView) findViewById(R.id.detContact);

            tvName.setText(legDetail.get(0));
            tvEmail.setText(legDetail.get(1));
            tvChamber.setText(legDetail.get(2));
            tvContact.setText(legDetail.get(3));
        }
    }
}
