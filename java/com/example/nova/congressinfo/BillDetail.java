package com.example.nova.congressinfo;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by NOVA on 16/11/22.
 */

public class BillDetail extends AppCompatActivity {
    String id;
    List<String> billDetail;
    TextView tvId;
    TextView tvTitle;
    TextView tvType;
    TextView tvSponsor;
    TextView tvChamber;
    TextView tvStatus;
    TextView tvIntroduced;
    TextView tvConUrl;
    TextView tvVerStatus;
    TextView tvBillUrl;
    ImageButton imgBBillFav;
    SharedPreferences sharedPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Bill Info");
        id = getIntent().getStringExtra("billId").toLowerCase();


        DetBTask detBTask = new DetBTask();
        detBTask.execute("http://104.198.0.197:8080/bills?bill_id=" + id + "&apikey=3e10ee5ae4ca4e5f884cbedf3ef2372a");


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

    private class DetBTask extends AsyncTask<Object, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Object... params) {
            billDetail = new ArrayList<>();

            Object url = params[0];
            URLConnection connection;
            InputStream is=null;
            BufferedReader bis=null;
            try {
                connection = new URL((String) url).openConnection();
                is = connection.getInputStream();
                bis = new BufferedReader(new InputStreamReader(is));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bis.readLine()) != null) {
                    response.append(line);
                }

                String finalJson = response.toString();

                JSONObject billJson = new JSONObject(finalJson);
                JSONArray billRes = billJson.getJSONArray("results");

                JSONObject singleBill = billRes.getJSONObject(0);

                String billId = singleBill.getString("bill_id");
                String billTitle = singleBill.getString("official_title");
                String billType = singleBill.getString("bill_type");

                String sTitle = singleBill.getJSONObject("sponsor").getString("title");
                String sLastName = singleBill.getJSONObject("sponsor").getString("last_name");
                String sFirstName = singleBill.getJSONObject("sponsor").getString("first_name");

                String billSponsor = sTitle + ". " + sLastName + ", " + sFirstName;
                String billChamber = singleBill.getString("chamber");
                String billStatus = String.valueOf(singleBill.getJSONObject("history").getBoolean("active"));
                String billIntroduced = singleBill.getString("introduced_on");
                billIntroduced=DateFormat.dateFormat(billIntroduced);

                String billCongressUrl = singleBill.getJSONObject("urls").getString("congress");
                String billVerStatus = "N.A";
                String billUrl="N.A";
                if (singleBill.has("last_version")) {
                     billVerStatus = singleBill.getJSONObject("last_version").getString("version_name");
                     billUrl = singleBill.getJSONObject("last_version").getJSONObject("urls").getString("pdf");
                }


                billDetail.add(billId);
                billDetail.add(billTitle);
                billDetail.add(billType);
                billDetail.add(billSponsor);
                billDetail.add(billChamber);
                billDetail.add(billStatus);
                billDetail.add(billIntroduced);
                billDetail.add(billCongressUrl);
                billDetail.add(billVerStatus);
                billDetail.add(billUrl);

                return billDetail;

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
        protected void onPostExecute(List<String> bills) {
            super.onPostExecute(bills);

            tvId = (TextView) findViewById(R.id.detBbill_id);
            tvTitle = (TextView) findViewById(R.id.detBtitle);
            tvType = (TextView) findViewById(R.id.detBbill_type);
            tvSponsor = (TextView) findViewById(R.id.detBsponsor);
            tvChamber = (TextView) findViewById(R.id.detBchamber);
            tvStatus = (TextView) findViewById(R.id.detBstatus);
            tvIntroduced = (TextView) findViewById(R.id.detBinton);
            tvConUrl = (TextView) findViewById(R.id.detBconurl);
            tvVerStatus = (TextView) findViewById(R.id.detBvstatus);
            tvBillUrl = (TextView) findViewById(R.id.detBburl);
            imgBBillFav= (ImageButton) findViewById(R.id.imgBBillFav);


            tvId.setText(billDetail.get(0));
            tvTitle.setText(billDetail.get(1));
            tvType.setText(billDetail.get(2));
            tvSponsor.setText(billDetail.get(3));
            tvChamber.setText(billDetail.get(4));
            tvStatus.setText(billDetail.get(5));
            tvIntroduced.setText(billDetail.get(6));
            tvConUrl.setText(billDetail.get(7));
            tvVerStatus.setText(billDetail.get(8));
            tvBillUrl.setText(billDetail.get(9));

            sharedPref=getSharedPreferences("FavSp",MODE_PRIVATE);

            Boolean alreadyFav=false;
            Gson gson=new Gson();

            Set<String> set= sharedPref.getStringSet("favBillJson",new HashSet<String>());
            Iterator<String> itr = set.iterator();

            while(itr.hasNext()){
                String str = itr.next();
                Bill b = gson.fromJson(str, Bill.class);
                String tmpid=b.getBillId();
                if(tmpid.equals(id.toUpperCase())){
                    alreadyFav=true;
                    break;
                }
            }


            if (!alreadyFav){
                imgBBillFav.setBackgroundResource(R.drawable.fav);

                imgBBillFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgBBillFav.setBackgroundResource(R.drawable.yellow);
                        SharedPreferences.Editor e=sharedPref.edit();
                        Gson gson = new Gson();

                        Bill favBill=new Bill(id.toUpperCase(),billDetail.get(1),billDetail.get(6));

                        String billJson = gson.toJson(favBill);

                        MainActivity.favBill.add(billJson);

                        e.putStringSet("favBillJson",MainActivity.favBill);
                        e.commit();


                    }
                });

            }else {

                imgBBillFav.setBackgroundResource(R.drawable.yellow);
                imgBBillFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgBBillFav.setBackgroundResource(R.drawable.fav);
                        SharedPreferences.Editor e=sharedPref.edit();
                        Gson gson = new Gson();

                        Bill favBill=new Bill(id.toUpperCase(),billDetail.get(1),billDetail.get(6));

                        String billJson = gson.toJson(favBill);
                        MainActivity.favBill.remove(billJson);

                        e.putStringSet("favBillJson",MainActivity.favBill);
                        e.commit();


                        Log.d("favbill",String.valueOf(MainActivity.favBill.size()));

//
                    }
                });
            }




        }
    }


}
