package com.hamza.inventory.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.inventory.Adapters.Customer_Addapter;
import com.hamza.inventory.Date_Models.Customer_model;
import com.hamza.inventory.Fragments.DrawerFragment;
import com.hamza.inventory.Network.EndPoints;
import com.hamza.inventory.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Customers extends AppCompatActivity {

    ListView listView;
    Customer_Addapter customer_addapter = null;
    Toolbar toolbar;
    String heading,id;
    DrawerFragment drawerFragment = new DrawerFragment();
    ImageView addcustomer;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    private ArrayList<Customer_model> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);



        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Customers");


        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        drawerFragment.setup((DrawerLayout) findViewById(R.id.drawerlayout), toolbar);

        String[] buss_name = new String[] {"Test Bussines 1","Test Bussines 2"};
        String[] mobile = new String[] {"03201234556","03214567891"};
        String[] adress = new String[] {"Test Adress 1","Test Adress 2"};


        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Prefs", MODE_PRIVATE);

        id=  pref.getString("id", null);

        Intent intent = getIntent();
        heading = intent.getStringExtra("from");

        listView = (ListView) findViewById(R.id.customer_list);
        addcustomer = (ImageView) findViewById(R.id.addcustomer);
        customer_addapter = new Customer_Addapter(getApplicationContext(), R.layout.row_customer, list, this);


        getCustomer();


        addcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Customers.this,Add_New_Customer.class);
                finish();
                startActivity(intent);

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(heading.equals("sale"))
                {
                    Intent intent= new Intent(Customers.this,Products.class);
                    finish();
                    startActivity(intent);
                }

                if(heading.equals("recovry"))
                {
                    Intent intent= new Intent(Customers.this,Recovery.class);
                    finish();
                    startActivity(intent);
                }
                if(heading.equals("balance"))
                {
                    Toast.makeText(Customers.this, " pop up with balance", Toast.LENGTH_SHORT).show();
                }

                if (heading.equals("sample"))
                {

                    Intent intent= new Intent(Customers.this,Sample.class);
                    finish();
                    startActivity(intent);

                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void getCustomer() {

        ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        String URL =null;

        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.GET_CUSTOMER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                       // Toast.makeText(Customers.this, response, Toast.LENGTH_SHORT).show();


                        parseJSONResponce(response);




                        listView.setAdapter(customer_addapter);

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
                params.put("user_id",id);


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
                String bussines_name = Information.getString("bussiness_name");
                String personal_name = Information.getString("personal_name");
                String address = Information.getString("address");
                String distrcit = Information.getString("distrcit");
                String mobile = Information.getString("mobile");


                Customer_model data = new Customer_model();
                data.setAdress(address);
                data.setB_name(bussines_name);
                data.setDistrcit(distrcit);
                data.setMobile(mobile);
                data.setPeronal_name(personal_name);
                data.setId(id);

                 list.add(data);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
