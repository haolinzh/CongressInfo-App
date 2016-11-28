package com.example.nova.congressinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by NOVA on 16/11/27.
 */

public class AboutmeActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("click","abooutme2");
        setTitle("About Me");
        setContentView(R.layout.fragment_aboutme);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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
