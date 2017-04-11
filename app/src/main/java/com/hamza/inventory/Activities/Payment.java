package com.hamza.inventory.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.inventory.Network.EndPoints;
import com.hamza.inventory.R;
import com.hamza.inventory.SQLite_DB.Database;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {


    Toolbar toolbar;
    Button send,calculate;
    EditText amount,remainingamount;
    TextView Total;
    String sAmount,sRemianing,id,sColums,Sales,prinitnng;
    Date date;
    Database database = new Database(this);
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
     float total;
    float remaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Payment");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Prefs", MODE_PRIVATE);
        id=  pref.getString("id", null);
        Sales = pref.getString("sales",null);

        send = (Button) findViewById(R.id.send_rec);
        amount = (EditText) findViewById(R.id.name);
        remainingamount = (EditText) findViewById(R.id.password);
        calculate = (Button) findViewById(R.id.calculate);
        Total = (TextView) findViewById(R.id.total_amount);


        getValus();

        float def = (float) 0.0;
        Intent intent = getIntent();
        total = intent.getFloatExtra("total_amount",def);
        Sales = intent.getStringExtra("sales");
      //  sColums = intent.getStringExtra("colums");

        prinitnng =Sales;



       Total.setText(total+"");



        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getValus();

                if(sAmount.equals("") || sAmount == null)
                {
                    Toast.makeText(Payment.this, "No Amount Entered ", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(sAmount)>total)
                {
                    Toast.makeText(Payment.this, "Your entered amount is greater then total amount", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    remaining = total -Integer.parseInt(sAmount);
                    remainingamount.setText(remaining+"");

                }


            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValus();

                if(remainingamount.getText().toString().equals(""))
                {
                    Toast.makeText(Payment.this, "Please Calculate the remaining amount !! ", Toast.LENGTH_SHORT).show();
                }

                else
                {
                   /* DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    date= new Date();*/
                    Sales = Sales+"{"+total+","+sAmount+","+remaining;

                    //paymnet();

                    EnterSales();

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("Buss_details", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("total",Total.getText().toString());
                    editor.putString("remaining",remainingamount.getText().toString());
                    editor.putString("paid",sAmount+"");
                    editor.commit();

                    /*Intent intent = new Intent(Payment.this,Printer.class);
                    intent.putExtra("from","sales");
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
        finish();
        return super.onOptionsItemSelected(item);


    }
    public void getValus()
    {

        sRemianing = remainingamount.getText().toString();
        sAmount = amount.getText().toString();

    }

    public void EnterSales() {

        ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        String URL =null;

        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.BASE_URL+"Welcome/sale",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        ringProgressDialog.dismiss();

                        if(response.equals(""))
                        {
                            Toast.makeText(Payment.this, "Records not entered ! Some thing went wrong", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        else
                        {
                            Toast.makeText(Payment.this, "Entered Sucessfully", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Payment.this, Printer.class);
                            intent.putExtra("sale",prinitnng);
                            startActivity(intent);


                           /* if (checkBox.isChecked()) {*/

                            /*Intent intent = new Intent(Payment.this, Payment.class);
                            intent.putExtra("sales",Sales);
                            intent.putExtra("colums",response);
                            intent.putExtra("total_amount",total);
                            startActivity(intent);*/


                          /*  }
                            else
                            {


                            }*/






                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                String message = null;
                if (error instanceof NoConnectionError)
                {


                    Toast.makeText(Payment.this, "No Internet Connection !! Adding to local DataBase", Toast.LENGTH_LONG).show();

                    database.insertSales(Sales);



                    Intent intent = new Intent(Payment.this, Printer.class);
                    intent.putExtra("sale", prinitnng);
                    startActivity(intent);

                    // Intent intent = new Intent(Sales.this, Printer.class);
                    // intent.putExtra("sale",Sales);
                    // startActivity(intent);

                } else if (error instanceof TimeoutError) {
                   //ringProgressDialog.dismiss();
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(Payment.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("sales",Sales);


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


    /*public void paymnet() {

        ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();



        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.UPDATE_RECORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {

                        ringProgressDialog.dismiss();
                           if(response.equals("")||response ==  null)
                           {
                               Toast.makeText(Payment.this, "Not Entered", Toast.LENGTH_SHORT).show();
                           }
                            else
                           {
                               Toast.makeText(Payment.this, " Entered Succes", Toast.LENGTH_SHORT).show();
                              // EnterSales();
                           }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog.dismiss();


                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(Payment.this,"No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else if (error instanceof TimeoutError) {


                    Toast.makeText(Payment.this, "Connection TimeOut! Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("amount", sAmount);
                params.put("total",Total.getText().toString());
                params.put("Remaining", sRemianing);
                *//*params.put("ids", sColums);
                params.put("buss_id", id);
                params.put("date", date.toString());*//*


                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }*/





}


