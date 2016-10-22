package com.hamza.inventory.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.hamza.inventory.Adapters.Sample_Addapter;
import com.hamza.inventory.R;

public class Sample extends AppCompatActivity {

    Toolbar toolbar;
    Button send;
    ImageView add;
    ListView sample_list;
    Sample_Addapter sample_addapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String[] product = new String[] {"Product 1","Product 2"};
        String[] quantity = new String[] {"50","40"};
        String[] rate = new String[] {"400","250"};



        toolbar.setTitle("Sample");

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        send = (Button) findViewById(R.id.send_button);
        add = (ImageView) findViewById(R.id.add_sample);
        sample_list = (ListView) findViewById(R.id.sample_list);

        sample_addapter = new Sample_Addapter(Sample.this,product,rate,quantity);

        sample_list.setAdapter(sample_addapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sample.this, Add_Products.class);
                startActivity(intent);
            }
        });




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

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
