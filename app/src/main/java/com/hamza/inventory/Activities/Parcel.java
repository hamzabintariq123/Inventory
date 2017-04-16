package com.hamza.inventory.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import com.hamza.inventory.Adapters.Parcel_Addapter;
import com.hamza.inventory.Date_Models.Parcel_model;
import com.hamza.inventory.Network.EndPoints;
import com.hamza.inventory.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parcel extends AppCompatActivity {

    Toolbar toolbar;
    ListView parcel_list;
    ProgressDialog ringProgressDialog;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    String id;
    private ArrayList<Parcel_model> list = new ArrayList<>();
    Parcel_Addapter parcel_addapter = null ;
    final Context context= this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Sales");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Prefs", MODE_PRIVATE);
        id = pref.getString("id",null);



        parcel_list = (ListView) findViewById(R.id.parcel);

        getParcle();

        parcel_addapter = new Parcel_Addapter(getApplicationContext(),R.layout.row_parcel,list,this);


        parcel_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showDetail(list.get(position).getProduct(),list.get(position).getQuantity(),list.get(position).getProduct_date());

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

        Intent intent= new Intent(Parcel.this, Customers.class);
        intent.putExtra("from", "sale");
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);


    }
    public void getParcle() {

        ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        String URL =null;

        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.GET_PARCEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        parseJSONResponce(response);

                        parcel_list.setAdapter(parcel_addapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError)
                {

                    ringProgressDialog.dismiss();


                } else if (error instanceof TimeoutError) {

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(Parcel.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id",id);

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

    public void parseJSONResponce(String responce)
    {
        try {

            JSONArray array = new JSONArray(responce);

            for (int i = 0; i < array.length(); i++) {

                JSONObject Information = array.getJSONObject(i);

                String Quantity = Information.getString("Quantity");
                String Saleman_id = Information.getString("saleman_id");
                String Product = Information.getString("Product");
                String Product_date = Information.getString("product_date");
                String Retail_price = Information.getString("retail_price");
                String Trade_price = Information.getString("trade_price");

                Parcel_model data = new Parcel_model();
                data.setSaleman_id(Saleman_id);
                data.setProduct(Product);
                data.setProduct_date(Product_date);
                data.setRetail_price(Retail_price);
                data.setTrade_price(Trade_price);
                data.setQuantity(Quantity);

                list.add(data);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    void showDetail (final String strname,final String strqty,final String strdate)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.remaining, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final TextView name = (TextView) dialogView.findViewById(R.id.name);
        final TextView qty = (TextView) dialogView.findViewById(R.id.quantty);
        final TextView date = (TextView) dialogView.findViewById(R.id.date_send);

        Button submit = (Button) dialogView.findViewById(R.id.okbutton);

        name.setText(strname);
        qty.setText(strqty);
        date.setText(strdate);


        final AlertDialog b = dialogBuilder.create();
        b.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });


    }


}
