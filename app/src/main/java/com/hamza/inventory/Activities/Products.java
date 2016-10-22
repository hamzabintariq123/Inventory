package com.hamza.inventory.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.hamza.inventory.Adapters.Product_Addapter;
import com.hamza.inventory.R;

public class Products extends AppCompatActivity {


    Toolbar toolbar;
    Button send;
    ImageView add;
    ListView product_list;
    CheckBox checkBox;
    Product_Addapter product_addapter = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Products");

        String[] product = new String[] {"Product 1","Product 2"};
        String[] quantity = new String[] {"50","40"};
        String[] rate = new String[] {"400","250"};
        String[] amount = new String[] {"20000","10000"};

        add = (ImageView) findViewById(R.id.add);
        send = (Button) findViewById(R.id.send_rec);
        checkBox = (CheckBox) findViewById(R.id.pay_check);
        product_list = (ListView) findViewById(R.id.sample_list);

        product_addapter = new Product_Addapter(Products.this,product,quantity,rate,amount);

        product_list.setAdapter(product_addapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Products.this, Add_Products.class);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    Intent intent = new Intent(Products.this, Payment.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Products.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });


        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
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
