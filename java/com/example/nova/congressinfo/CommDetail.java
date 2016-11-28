package com.example.nova.congressinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comm_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Committee Info");
        Comm comm = (Comm) getIntent().getSerializableExtra("comm");

        tvId= (TextView) findViewById(R.id.detCid);
        tvName= (TextView) findViewById(R.id.detCname);
        tvChamber= (TextView) findViewById(R.id.detCchamber);
        tvPid= (TextView) findViewById(R.id.detCpid);
        tvContact= (TextView) findViewById(R.id.detCcontact);
        tvOffice= (TextView) findViewById(R.id.detCoffice);


        tvId.setText(comm.getCommId());
        tvName.setText(comm.getCommName());
        tvChamber.setText(comm.getCommChamber());
        tvPid.setText(comm.getCommPid());
        tvContact.setText(comm.getCommPhone());
        tvOffice.setText(comm.getCommOffice());



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
