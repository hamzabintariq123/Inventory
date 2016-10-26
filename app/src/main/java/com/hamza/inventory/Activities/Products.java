package com.hamza.inventory.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.inventory.Adapters.Product_Addapter;
import com.hamza.inventory.Date_Models.Customer_model;
import com.hamza.inventory.Date_Models.Sale_model;
import com.hamza.inventory.Network.EndPoints;
import com.hamza.inventory.R;
import com.hamza.inventory.SQLite_DB.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Products extends AppCompatActivity {


    Toolbar toolbar;
    Button send;
    ImageView add;
    ListView product_list;
    CheckBox checkBox;
    TextView total;
    SQLiteDatabase db;
    Database database;
    int total_am;
    JSONObject jObjSaleModel =  new JSONObject();
    Product_Addapter product_addapter = null;
    Sale_model objSaleModel = new Sale_model();
    static ArrayList<Sale_model> arrSaleData = new ArrayList<>();
    static JSONArray arrJsonSaleData = new JSONArray();
    ProgressDialog ringProgressDialog;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);


        try {
            database=database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Products");

        Intent i = getIntent();
        int rate = i.getIntExtra("rate",0);
        int quantity = i.getIntExtra("quantity",0);
        String strProduct = i.getStringExtra("productName");
        String strTotal = i.getStringExtra("total");

        add = (ImageView) findViewById(R.id.add);
        send = (Button) findViewById(R.id.send_rec);
        checkBox = (CheckBox) findViewById(R.id.pay_check);
        product_list = (ListView) findViewById(R.id.sample_list);
        total = (TextView) findViewById(R.id.total_amount);

        if(i.hasExtra("rate"))
        {
            objSaleModel.setProductName(strProduct);
            objSaleModel.setProductRate(String.valueOf(rate));
            objSaleModel.setProductAmount(String.valueOf(strTotal));
            objSaleModel.setProductQuantity(String.valueOf(quantity));

            try {
                jObjSaleModel.put("product_name" , strProduct);
                jObjSaleModel.put("product_rate" , String.valueOf(rate));
                jObjSaleModel.put("product_amount" , String.valueOf(strTotal));
                jObjSaleModel.put("product_quantity" , String.valueOf(quantity));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            arrSaleData.add(objSaleModel);
            arrJsonSaleData.put(jObjSaleModel);


            for (int j=0;j<arrSaleData.size();j++)
            {
                total_am = total_am+Integer.parseInt(arrSaleData.get(j).getProductAmount());

            }

            total.setText(total_am+"");

       /* String[] product = new String[] {"Product 1","Product 2"};
        String[] quantity = new String[] {"50","40"};
        String[] rate = new String[] {"400","250"};
        String[] amount = new String[] {"20000","10000"};
*/
            product_addapter = new Product_Addapter(Products.this,arrSaleData);

            product_list.setAdapter(product_addapter);
        }





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


                    for (int j=0; j<arrSaleData.size();j++)
                    {
                      //  database.insertSales()
                    }
                    String strJsonSaleData = arrJsonSaleData.toString();

                    //send strJsonSaleData in string reques

                    Intent intent = new Intent(Products.this, Payment.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Products.this, Customers.class);
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
