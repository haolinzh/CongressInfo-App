package com.example.nova.congressinfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.example.nova.congressinfo.R.drawable.s;

/**
 * Created by NOVA on 16/11/27.
 */

public class CommDetail extends AppCompatActivity {
    String id;
    TextView tvId;
    TextView tvName;
    TextView tvChamber;
    TextView tvPid;
    TextView tvContact;
    TextView tvOffice;
    SharedPreferences sharedPref;
    ImageButton imgCCommFav;
    ImageView imgCCommChamber;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comm_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Committee Info");
        final Comm comm = (Comm) getIntent().getSerializableExtra("comm");

        tvId= (TextView) findViewById(R.id.detCid);
        tvName= (TextView) findViewById(R.id.detCname);
        tvChamber= (TextView) findViewById(R.id.detCchamber);
        tvPid= (TextView) findViewById(R.id.detCpid);
        tvContact= (TextView) findViewById(R.id.detCcontact);
        tvOffice= (TextView) findViewById(R.id.detCoffice);
        imgCCommFav= (ImageButton) findViewById(R.id.imgCCommFav);
        imgCCommChamber= (ImageView) findViewById(R.id.commDetChamber);

        

        tvId.setText(comm.getCommId());
        tvName.setText(comm.getCommName());
        tvChamber.setText(Character.toUpperCase(comm.getCommChamber().charAt(0)) + comm.getCommChamber().substring(1));
        tvPid.setText(comm.getCommPid());
        tvContact.setText(comm.getCommPhone());
        tvOffice.setText(comm.getCommOffice());

        if (comm.getCommChamber().equals("house")){
            Picasso.with(getApplicationContext()).load(R.drawable.h).resize(60,60).into(imgCCommChamber);
        }else {
            Picasso.with(getApplicationContext()).load(s).resize(60,60).into(imgCCommChamber);
        }



        sharedPref=getSharedPreferences("FavSp",MODE_PRIVATE);

        Boolean alreadyFav=false;
        Gson gson=new Gson();

        Set<String> set= sharedPref.getStringSet("favCommJson",new HashSet<String>());
        Iterator<String> itr = set.iterator();
        id=comm.getCommId();

        while(itr.hasNext()){
            String str = itr.next();
            Comm c = gson.fromJson(str, Comm.class);
            String tmpid=c.getCommId();

            Log.d("id1",id);
            Log.d("id in set",tmpid);

            if(tmpid.equals(id)){
                alreadyFav=true;
                break;
            }
        }


        if (!alreadyFav){
            imgCCommFav.setBackgroundResource(R.drawable.fav);

            imgCCommFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgCCommFav.setBackgroundResource(R.drawable.yellow);
                    SharedPreferences.Editor e=sharedPref.edit();
                    Gson gson = new Gson();

                    String commJson = gson.toJson(comm);

                    MainActivity.favComm.add(commJson);

                    e.putStringSet("favCommJson", MainActivity.favComm);
                    e.commit();

                    Log.d("favcomm",String.valueOf(MainActivity.favComm.size()));

                }
            });

        }else {

            imgCCommFav.setBackgroundResource(R.drawable.yellow);
            imgCCommFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgCCommFav.setBackgroundResource(R.drawable.fav);
                    SharedPreferences.Editor e=sharedPref.edit();
                    Gson gson = new Gson();

                    String billJson = gson.toJson(comm);

                    MainActivity.favComm.remove(billJson);

                    e.putStringSet("favCommJson",   MainActivity.favComm);
                    e.commit();


                    Log.d("favcomm",String.valueOf(MainActivity.favComm.size()));


                }
            });
        }



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




}
