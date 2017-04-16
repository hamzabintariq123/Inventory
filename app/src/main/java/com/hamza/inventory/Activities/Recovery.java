package com.hamza.inventory.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.inventory.Network.EndPoints;
import com.hamza.inventory.R;
import com.hamza.inventory.SQLite_DB.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Recovery extends AppCompatActivity {


    Toolbar  toolbar;
    Button rec_amount;
    EditText recovry_amount;
    String recovry,remining,buss_id,id;
    TextView Piad, Remaining, Total;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    int remaining,paid;
    SQLiteDatabase db;
    Database database = new Database(this);

    String businnes_name  ;
    String total_bill;
    String amount_remaining ;
    String amount_paid ;
    String salesman_id ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Recovry");


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        Intent intent= getIntent();
        buss_id = intent.getStringExtra("buss_id");

        getBalance(buss_id);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Prefs", MODE_PRIVATE);

        id=  pref.getString("id", null);


        try {
            database=database.open();
         }catch (SQLException e) {
            e.printStackTrace();
        }


        Total = (TextView) findViewById(R.id.total);
        Remaining = (TextView) findViewById(R.id.quantty);
        Piad = (TextView) findViewById(R.id.date_send);
        rec_amount = (Button) findViewById(R.id.send_rec);
        recovry_amount = (EditText) findViewById(R.id.name);




        rec_amount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                getValues();


                if(Remaining.getText().toString().equals("")||Piad.getText().toString().equals("")||
                Remaining.getText().toString() == null||Piad.getText().toString()==null
                       || recovry_amount.getText().toString().equals("")||recovry_amount.getText().toString() == null)
                {
                    Toast.makeText(Recovery.this, "No values Entered", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    remaining  = Integer.parseInt(Remaining.getText().toString());
                    paid = Integer.parseInt(Piad.getText().toString());
                    int strrecovry = Integer.parseInt(recovry_amount.getText().toString());


                        paid = paid +Integer.parseInt(recovry);
                        remaining = remaining-Integer.parseInt(recovry);

                        sendRecovry();

                        Intent intent = new Intent(Recovery.this,Customers.class);
                        intent.putExtra("from","recovry");
                        startActivity(intent);

                }


            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Recovery.this,Customers.class);
        intent.putExtra("from","recovry");
        startActivity(intent);
        return super.onOptionsItemSelected(item);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void  getValues()
    {
        recovry = recovry_amount.getText().toString();

    }

    public void getBalance( final String buss_id) {

        ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        String URL =null;

        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.GET_BALANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        // Toast.makeText(Customers.this, response, Toast.LENGTH_SHORT).show();


                        try {

                            if (!response.equals("[]")) {


                                JSONArray array = new JSONArray(response);


                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject Information = array.getJSONObject(i);

                                    String paid = Information.getString("amount_paid");
                                    String remaining = Information.getString("amount_remaining");
                                    String total = Information.getString("total_bill");


                                    String id = Information.getString("id");
                                    Remaining.setText(remaining);
                                    Total.setText(total);
                                    Piad.setText(paid);

                                    database.insertRecovry(
                                            buss_id,
                                            Integer.valueOf(total),
                                            Integer.valueOf(paid),
                                            Integer.valueOf(remaining),
                                            Integer.valueOf(id));


                                }

                            }
                            else{

                                Toast.makeText(Recovery.this, "No record For this bussines ", Toast.LENGTH_SHORT).show();

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();


                Cursor cus = database.getRecords("recovry", buss_id);
                cus.moveToFirst();
                int count = cus.getColumnCount();
                if (cus != null && cus.getCount() > 0) {
                     /* int count = cus.getColumnCount();
                        Toast.makeText(Recovery.this, count+"", Toast.LENGTH_SHORT).show();*/
                    id = cus.getString(0);
                    businnes_name = cus.getString(1);
                    total_bill = cus.getString(4);
                    amount_remaining = cus.getString(3);
                    amount_paid = cus.getString(2);
                    salesman_id = cus.getString(5);


                    Remaining.setText(amount_remaining);
                    Total.setText(total_bill);
                    Piad.setText(amount_paid);
                } else {

                    Toast.makeText(Recovery.this, "No record found", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(Recovery.this,"No Internet Connection !! Loading from Local Data Base", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",buss_id);


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


    public void sendRecovry( ) {

        ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        String URL =null;

        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.SEND_RECOVRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        Toast.makeText(Recovery.this, "Recovery done succesfully", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError)
                {

                    database.updateRecovry(remaining,paid,buss_id);

                    Toast.makeText(Recovery.this, "No Internet Connection !! adding to local database !!", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recovry",paid+"");
                params.put("remaining",remaining+"");
                params.put("buss_id",buss_id);


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
