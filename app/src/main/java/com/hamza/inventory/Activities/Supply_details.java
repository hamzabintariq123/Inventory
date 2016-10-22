package com.hamza.inventory.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.hamza.inventory.Adapters.Recovry_Addapter;
import com.hamza.inventory.R;

public class Supply_details extends AppCompatActivity {

    Toolbar toolbar;
    Recovry_Addapter recovry_addapter = null;
    ListView supply_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_details);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] product = new String[] {"Product 1","Product 2"};
        String[] quantity = new String[] {"50","40"};

        toolbar.setTitle("Supply");

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        supply_list = (ListView) findViewById(R.id.supply_list);


        recovry_addapter =  new Recovry_Addapter(Supply_details.this,product,quantity);

        supply_list.setAdapter(recovry_addapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);


    }
}
