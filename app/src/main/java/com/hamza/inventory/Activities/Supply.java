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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.hamza.inventory.Date_Models.Products_model;
import com.hamza.inventory.Network.EndPoints;
import com.hamza.inventory.R;
import com.hamza.inventory.SQLite_DB.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Supply extends AppCompatActivity {

    Toolbar toolbar;
    Spinner product,discount_sppiner;
    EditText productQuantity,productRate;
    TextView Total;
    RadioGroup productsRate;
    String price,discount;
    ProgressDialog ringProgressDialog;
    String  strProduct,from,salesid;
    Button return_cus,calcutle;
    String total="",strbuss_id;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    private ArrayList<Products_model> list = new ArrayList<>();
    SQLiteDatabase db;
    Database database = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply);

        toolbar = (Toolbar) findViewById(R.id.app_bar);

        toolbar.setTitle("Supply");


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);


        product = (Spinner) findViewById(R.id.product_spinner);
        productQuantity = (EditText) findViewById(R.id.quantity);
        productRate = (EditText) findViewById(R.id.rate);
        Total = (TextView) findViewById(R.id.total);
        productsRate=(RadioGroup) findViewById(R.id.radioGroup);
        discount_sppiner = (Spinner) findViewById(R.id.discount_sppiner);
        return_cus = (Button) findViewById(R.id.return_customer);
        calcutle = (Button) findViewById(R.id.cal_amount);


        try {
            database=database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getProducts();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Prefs", MODE_PRIVATE);

        salesid=  pref.getString("id", null);
        Intent i = getIntent();
        strbuss_id = i.getStringExtra("buss_id");
        calcutle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(productRate.getText().toString().equals("") || productQuantity.getText().toString().equals(""))
                {
                    Toast.makeText(Supply.this, "Please Add the Fields To calculate Amount", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    int rate = Integer.parseInt(productRate.getText().toString());
                    int quantity = Integer.parseInt(productQuantity.getText().toString());

                    // Calculating Discount
                    if(discount != "0")
                    {
                        int amount  = rate*quantity;
                        int discount_calculate = Integer.parseInt(discount);
                        discount_calculate = ((amount)/100)*discount_calculate;
                        amount = amount-discount_calculate;
                        total = String.valueOf(amount);
                        Total.setText(total);

                    }
                    else
                    {
                        int amount  = rate*quantity;
                        total = String.valueOf(amount);
                        Total.setText(total);
                    }

                }




            }
        });

            return_cus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(total.equals("") || total == null)
                    {
                        Toast.makeText(Supply.this, "Pleae Calculate the amount first", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        returnCustomer();
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


    public void getProducts() {

        ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        String URL =null;

        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.GET_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        parseJSONResponce(response);

                        setSpinner();




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;

                ringProgressDialog.dismiss();

                if (error instanceof NoConnectionError)
                {

                    Toast.makeText(Supply.this, "No internet Connection !! Loading from local database", Toast.LENGTH_SHORT).show();
                    list = database.getAllProductss();

                    setSpinner();

                } else if (error instanceof TimeoutError) {

                    list = database.getAllProductss();

                    setSpinner();

                    message = "Connection TimeOut! Loading from local data base";
                    Toast.makeText(Supply.this,message, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sales_id",salesid);


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
    private void setSpinner() {


        List<String> product_list = new ArrayList<String>();
        List<String> discount_list = new ArrayList<String>();

        discount_list.add("0");
        discount_list.add("5");
        discount_list.add("10");
        discount_list.add("15");
        discount_list.add("20");
        discount_list.add("25");


        for (int i = 0; i < list.size(); i++) {
            product_list.add(list.get(i).getName());
        }



        // Setting product spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, product_list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        product.setAdapter(dataAdapter);

        product.setOnItemSelectedListener(new CustomOnItemSelectedListener_Product());


        //  setting discount_sppiner spinner
        ArrayAdapter<String> discountAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, discount_list);

        discountAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        discount_sppiner.setAdapter(discountAdapter);

        discount_sppiner.setOnItemSelectedListener(new CustomOnItemSelectedListener_discount());
    }

    public class CustomOnItemSelectedListener_Product implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {

            strProduct = parent.getItemAtPosition(pos).toString();





            productsRate=(RadioGroup) findViewById(R.id.radioGroup);
            price =  list.get(pos).getRetail();
            productsRate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.retail:
                            price =  list.get(pos).getRetail();
                            productRate.setText(price);
                            break;
                        case R.id.trade:
                            price =  list.get(pos).getTrade();
                            productRate.setText(price);
                            break;
                    }
                }
            });

            productRate.setText(price);
        }



        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }


    public class CustomOnItemSelectedListener_discount implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {

            discount = parent.getItemAtPosition(pos).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }

    public void parseJSONResponce(String responce)
    {
        try {

            JSONArray array = new JSONArray(responce);


            for (int i = 0; i < array.length(); i++) {

                JSONObject Information = array.getJSONObject(i);

                String id = Information.getString("id");
                String product_name = Information.getString("Product");
                String retail_price = Information.getString("retail_price");
                String trade_price = Information.getString("trade_price");
                String quantity = Information.getString("Quantity");



                Products_model data = new Products_model();

                data.setId(id);
                data.setName(product_name);
                data.setRetail(retail_price);
                data.setTrade(trade_price);
                data.setQuantay(quantity);

                list.add(data);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void returnCustomer() {

        ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();



        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.RETURN_CUSTOMER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        Intent intent = new Intent(Supply.this,Customers.class);
                        intent.putExtra("from","supply");
                        startActivity(intent);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;

                ringProgressDialog.dismiss();

                if (error instanceof NoConnectionError)
                {

                    Toast.makeText(Supply.this, "No internet Connection !! Loading from local database", Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {


                    message = "Connection TimeOut! Loading from local data base";
                    Toast.makeText(Supply.this,message, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("return",Total.getText().toString());
                params.put("quantity",productQuantity.getText().toString());
                params.put("sales_id",salesid);
                params.put("buss_id",strbuss_id);
                params.put("product",strProduct);


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
