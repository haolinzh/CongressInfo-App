package com.example.nova.congressinfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
    TextView tvSterm;
    TextView tvEterm;
    TextView tvOffice;
    TextView tvState;
    TextView tvFax;
    TextView tvBirthday;
    TextView tvParty;
    ImageView imgParty;
    ProgressBar termProgress;

    ImageView imgDet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leg_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Legislator Info");
        id = getIntent().getStringExtra("legId");

        Log.d("id",id);

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


    private int calTermPercent(String start,String end){
        Timestamp tStart;
        Timestamp tEnd;
        start=start+" 00:00:00";
        end=end+" 00:00:00";
        tStart=Timestamp.valueOf(start);
        tEnd=Timestamp.valueOf(end);
        long tStarttime=tStart.getTime();
        long tEndtime=tEnd.getTime();
        Date date=new Date();
        long tNowtime=date.getTime();
        int percent=(int)((tNowtime-tStarttime)*100/(tEndtime-tStarttime));

        return percent;
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
                JSONObject singleLeg = legRes.getJSONObject(0);

                String legTitle = singleLeg.getString("title");
                String legFirstName = singleLeg.getString("first_name");
                String legLastName = singleLeg.getString("last_name");

                String legName = legTitle+". "+legFirstName + ", " + legLastName;
                String legEmail=singleLeg.getString("oc_email");
                String legChamber=singleLeg.getString("chamber");
                String legContact=singleLeg.getString("phone");
                String legSTerm=singleLeg.getString("term_start");
                String legETerm=singleLeg.getString("term_end");
                String legOffice=singleLeg.getString("office");
                String legState=singleLeg.getString("state");
                String legFax=singleLeg.getString("fax");
                String legBirthday=singleLeg.getString("birthday");
                String legParty = singleLeg.getString("party");


                legDetail.add(legName);
                legDetail.add(legEmail);
                legDetail.add(legChamber);
                legDetail.add(legContact);
                legDetail.add(legSTerm);
                legDetail.add(legETerm);
                legDetail.add(legOffice);
                legDetail.add(legState);
                legDetail.add(legFax);
                legDetail.add(legBirthday);
                legDetail.add(legParty);

                return legDetail;


            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;


        }


        @Override
        protected void onPostExecute(List<String> legs) {
            super.onPostExecute(legs);
            tvName= (TextView) findViewById(R.id.detName);
            tvEmail= (TextView) findViewById(R.id.detEmail);
            tvChamber= (TextView) findViewById(R.id.detChamber);
            tvContact= (TextView) findViewById(R.id.detContact);
            tvSterm= (TextView) findViewById(R.id.detSTerm);
            tvEterm= (TextView) findViewById(R.id.detETerm);
            tvOffice= (TextView) findViewById(R.id.detOffice);
            tvState= (TextView) findViewById(R.id.detState);
            tvFax= (TextView) findViewById(R.id.detFax);
            tvBirthday  = (TextView) findViewById(R.id.detBirthday);
            tvParty=(TextView)findViewById(R.id.detParty);
            imgParty=(ImageView) findViewById(R.id.imageViewParty);
            termProgress= (ProgressBar) findViewById(R.id.TermBar);
            imgDet= (ImageView) findViewById(R.id.ivDetLeg);

           int percent= calTermPercent(legDetail.get(4),legDetail.get(5));

            tvName.setText(legDetail.get(0));
            tvEmail.setText(legDetail.get(1));
            tvChamber.setText(legDetail.get(2));
            tvContact.setText(legDetail.get(3));
            tvSterm.setText(DateFormat.dateFormat(legDetail.get(4)));
            tvEterm.setText(DateFormat.dateFormat(legDetail.get(5)));
            tvOffice.setText(legDetail.get(6));
            tvState.setText(legDetail.get(7));
            tvFax.setText(legDetail.get(8));
            tvBirthday.setText(DateFormat.dateFormat(legDetail.get(9)));
            String p=legDetail.get(10);

            if (p.equals("R")){
                tvParty.setText("Republican");
                imgParty.setImageResource(R.drawable.r);

            }else if(p.equals("D")){
                tvParty.setText("Democratic");
                imgParty.setImageResource(R.drawable.d);
            }else {
                tvParty.setText("Independence");

            }

            String picUrl="https://theunitedstates.io/images/congress/original/"+id+".jpg";
            Picasso.with(getApplicationContext()).load(picUrl).resize(150,150).into(imgDet);
            termProgress.setProgress(percent);


        }
    }
}
