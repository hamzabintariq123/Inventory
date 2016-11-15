package com.hamza.inventory.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.hamza.inventory.Adapters.Product_Addapter;
import com.hamza.inventory.Adapters.Sample_Addapter;
import com.hamza.inventory.Date_Models.Sale_model;
import com.hamza.inventory.Network.EndPoints;
import com.hamza.inventory.R;
import com.hamza.inventory.SQLite_DB.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Sample extends AppCompatActivity {

    Toolbar toolbar;
    Button send;
    ImageView add;
    ListView sample_list;
    String strbuss_id;
    String Sales = "",user_id;
    SQLiteDatabase db;
    Database database = new Database(this);
    Sample_Addapter sample_addapter = null;
    Sale_model objSaleModel = new Sale_model();
    static ArrayList<Sale_model> arrSaleData = new ArrayList<>();
    static JSONArray arrJsonSaleData = new JSONArray();
    JSONObject jObjSaleModel =  new JSONObject();
    ProgressDialog ringProgressDialog;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i = getIntent();
        int rate = i.getIntExtra("rate", 0);
        int quantity = i.getIntExtra("quantity", 0);
        String strProduct = i.getStringExtra("productName");
        String strTotal = i.getStringExtra("Rotal");
        String strdiscount = i.getStringExtra("discount");
        strbuss_id = i.getStringExtra("buss_id");

        try {
            database=database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        toolbar.setTitle("Sample");

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Prefs", MODE_PRIVATE);
        user_id = pref.getString("id",null);

        send = (Button) findViewById(R.id.send_button);
        add = (ImageView) findViewById(R.id.add_sample);
        sample_list = (ListView) findViewById(R.id.sample_list);

        if (i.hasExtra("rate")) {
            objSaleModel.setProductName(strProduct);
            objSaleModel.setProductRate(String.valueOf(rate));
            objSaleModel.setProductQuantity(String.valueOf(quantity));

            try {
                jObjSaleModel.put("product_name", strProduct);
                jObjSaleModel.put("product_rate", String.valueOf(rate));
                jObjSaleModel.put("product_quantity" , String.valueOf(quantity));

            } catch (JSONException e) {
                e.printStackTrace();

            }
                arrSaleData.add(objSaleModel);
                arrJsonSaleData.put(jObjSaleModel);

                sample_addapter = new Sample_Addapter(Sample.this, arrSaleData);

                sample_list.setAdapter(sample_addapter);

            }
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Sample.this, Add_Products.class);
                    intent.putExtra("buss_id",strbuss_id);
                    intent.putExtra("from","sample");
                    startActivity(intent);
                }
            });


            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EnterSales();

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    for (int k =0 ; k<arrSaleData.size();k++)
                    {
                        Sales= Sales+"{"+user_id+","+strbuss_id+","+arrSaleData.get(k).getProductName()+","+arrSaleData.get(k).getProductRate()+","+
                                arrSaleData.get(k).getProductQuantity()+","+arrSaleData.get(k).getDiscount()+","+"Sample"+","+
                                "sample"+","+date;
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
        arrSaleData.clear();
        Intent intent = new Intent(Sample.this,Customers.class);
        intent.putExtra("from", "sample");
        startActivity(intent);
        return super.onOptionsItemSelected(item);
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
                            Toast.makeText(Sample.this, "Error in entring data ", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Sample.this, "Added Successfully ", Toast.LENGTH_SHORT).show();


                            SharedPreferences pref = getApplicationContext().getSharedPreferences("Buss_details", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("total","0   ");
                            editor.putString("remaining","0   ");
                            editor.putString("paid","0   ");
                            editor.commit();

                            Intent intent = new Intent(Sample.this, Printer.class);
                            intent.putExtra("sale",Sales);
                            startActivity(intent);

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NoConnectionError)
                {

                    ringProgressDialog.dismiss();
                    Toast.makeText(Sample.this, "NO Internet Connection !! Adding to local DataBase", Toast.LENGTH_SHORT).show();

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                        database.insertSales(Sales);

                    Intent intent = new Intent(Sample.this, Printer.class);
                    intent.putExtra("sale",Sales);
                    startActivity(intent);

                } else if (error instanceof TimeoutError) {

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(Sample.this, message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        arrSaleData.clear();
        Intent intent = new Intent(Sample.this,Customers.class);
        intent.putExtra("from", "sample");
        startActivity(intent);

        super.onBackPressed();
    }

}
