package com.hamza.inventory.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.inventory.Date_Models.Products_model;
import com.hamza.inventory.Date_Models.Sale_model;
import com.hamza.inventory.Network.EndPoints;
import com.hamza.inventory.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add_Products extends AppCompatActivity {

    Spinner product,discount;
    Toolbar toolbar;
    Button add_item;
    String heading,price;
    EditText productQuantity,productRate;
    TextView Total;
    RadioGroup productsRate;

    ProgressDialog ringProgressDialog;
    String  strProduct;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    private ArrayList<Products_model> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        Intent intent = getIntent();
        heading = intent.getStringExtra("from");

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Add Products");


        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        product = (Spinner) findViewById(R.id.product_spinner);
        add_item = (Button) findViewById(R.id.add_item);
        productQuantity = (EditText) findViewById(R.id.quantity);
        productRate = (EditText) findViewById(R.id.rate);
        Total = (TextView) findViewById(R.id.total);
        discount= (Spinner) findViewById(R.id.discount_sppiner);


        getProducts();


        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rate = Integer.parseInt(productRate.getText().toString());
                int quantity = Integer.parseInt(productQuantity.getText().toString());
               String productName = strProduct;


                Intent intent = new Intent(Add_Products.this,Products.class);
                intent.putExtra("from",heading);
                intent.putExtra("rate",rate);
                intent.putExtra("quantity",quantity);
                intent.putExtra("productName",productName);
                startActivity(intent);
            }
        });



    }

    private void setSpinner() {


        List<String> product_list = new ArrayList<String>();
        List<String> discount_list = new ArrayList<String>();

        discount_list.add("10%");
        discount_list.add("20%");
        discount_list.add("30%");
        discount_list.add("40%");
        discount_list.add("50%");


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


        //  setting discount spinner
        ArrayAdapter<String> discountAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, discount_list);

        discountAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        discount.setAdapter(discountAdapter);

        discount.setOnItemSelectedListener(new CustomOnItemSelectedListener_discount());
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

            String  discount = parent.getItemAtPosition(pos).toString();


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

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
                ringProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


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

                String id = Information.getString("id");
                String product_name = Information.getString("product_name");
                String retail_price = Information.getString("retail_price");
                String trade_price = Information.getString("trade_price");



                Products_model data = new Products_model();

                data.setId(id);
                data.setName(product_name);
                data.setRetail(retail_price);
                data.setTrade(trade_price);

                list.add(data);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
