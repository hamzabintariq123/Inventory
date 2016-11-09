package com.hamza.inventory.Activities;

import android.app.ProgressDialog;
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
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.inventory.Network.EndPoints;
import com.hamza.inventory.R;

import java.util.HashMap;
import java.util.Map;

public class Expence extends AppCompatActivity {

    Toolbar toolbar;
    EditText breakfsat, luch, dinner, fuel, rent, other, gifts;
    String sbreakfsat, sluch, sdinner, sfuel, srent, sother, sgifts;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expence);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Expence");

        breakfsat = (EditText) findViewById(R.id.breakfast);
        luch = (EditText) findViewById(R.id.luch);
        dinner = (EditText) findViewById(R.id.dinnet);
        fuel = (EditText) findViewById(R.id.fuel);
        rent = (EditText) findViewById(R.id.rent);
        other = (EditText) findViewById(R.id.others);
        gifts = (EditText) findViewById(R.id.gifts);
        add = (Button) findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getvalues();
                if (sbreakfsat.equals("") || sluch.equals("") || sdinner.equals("") || equals("") || sfuel.equals("")
                        || srent.equals("") || sother.equals("") || sgifts.equals("")) {
                    Toast.makeText(Expence.this, "Requied Field are empty", Toast.LENGTH_SHORT).show();
                } else {
                    addExpence();
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

    public void getvalues() {
        sbreakfsat = breakfsat.getText().toString();
        sluch = luch.getText().toString();
        sdinner = dinner.getText().toString();
        srent = rent.getText().toString();
        sother = other.getText().toString();
        sgifts = gifts.getText().toString();
        sfuel = fuel.getText().toString();

    }

    public void addExpence() {

        ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        String URL = null;

        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.ADD_EXPENCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(Expence.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {


                    Toast.makeText(Expence.this, "Connection TimeOut! Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("breakfast", sbreakfsat);
                params.put("lunch", sluch);
                params.put("dinner", sdinner);
                params.put("fuel", sfuel);
                params.put("rent", srent);
                params.put("other", sother);
                params.put("gifts", sgifts);

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
