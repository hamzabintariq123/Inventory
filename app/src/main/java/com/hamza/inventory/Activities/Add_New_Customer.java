package com.hamza.inventory.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.inventory.Network.EndPoints;
import com.hamza.inventory.R;

import java.util.HashMap;
import java.util.Map;

public class Add_New_Customer extends AppCompatActivity {


    Toolbar toolbar;
    Button add_customer;
    String heading, id ,strdistrict;
    EditText bussines, adress, mobile, name, district;
    String sbussines, sadress, smobile, sname, sdistrict;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__customer);

        add_customer = (Button) findViewById(R.id.add);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Add Customer");

        bussines = (EditText) findViewById(R.id.breakfast);
        adress = (EditText) findViewById(R.id.dinnet);
        mobile = (EditText) findViewById(R.id.fuel);
        name = (EditText) findViewById(R.id.luch);
        district = (EditText) findViewById(R.id.rent);


        Intent intent = getIntent();
        heading = intent.getStringExtra("from");


        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Prefs", MODE_PRIVATE);

        id = pref.getString("id", null);
        strdistrict = pref.getString("district", null);

        district.setText(strdistrict);




        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getValues();

                if (sbussines.equals("")||sadress.equals("")|| smobile.equals("")|| sname.equals("")|| sdistrict.equals(""))
                {
                    Toast.makeText(Add_New_Customer.this, "Required Fields are empty", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    newCustomer();
                /*    Intent intent = new Intent(Add_New_Customer.this, Customers.class);
                    finish();
                    intent.putExtra("from", heading);
                    startActivity(intent);*/
                }



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
        Intent intent = new Intent(Add_New_Customer.this, Customers.class);
        intent.putExtra("from","sale");
        startActivity(intent);
        return super.onOptionsItemSelected(item);


    }

    public void getValues() {


        sbussines = bussines.getText().toString();
        sadress = adress.getText().toString();
        smobile = mobile.getText().toString();
        sname = name.getText().toString();
        sdistrict = district.getText().toString();

    }

    public void newCustomer() {

        ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        String URL = null;

        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.ADD_CUSTOMER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        Toast.makeText(Add_New_Customer.this, response, Toast.LENGTH_SHORT).show();
                        finish();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("bussines", sbussines);
                params.put("adresss", sadress);
                params.put("mobile", smobile);
                params.put("name", sname);
                params.put("salesman", id);
                params.put("district", sdistrict);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }



}
