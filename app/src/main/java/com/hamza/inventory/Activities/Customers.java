package com.hamza.inventory.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.hamza.inventory.Adapters.Customer_Addapter;
import com.hamza.inventory.Date_Models.Customer_model;
import com.hamza.inventory.Fragments.DrawerFragment;
import com.hamza.inventory.Network.EndPoints;
import com.hamza.inventory.R;
import com.hamza.inventory.SQLite_DB.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Customers extends AppCompatActivity {

    ListView listView;
    Customer_Addapter customer_addapter = null;
    Customer_Addapter reload = null;
    Toolbar toolbar;
    EditText searchEDt;
    String heading,saleman_id,saleman_name;
    DrawerFragment drawerFragment = new DrawerFragment();
    ImageView addcustomer;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    private ArrayList<Customer_model> list = new ArrayList<>();
    SQLiteDatabase db;
    Database database = new Database(this);
    String sales,isFirst;
    ArrayList<String> List = new ArrayList<>();
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);




        try {
            database=database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Customers");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);




        drawerFragment.setup((DrawerLayout) findViewById(R.id.drawerlayout), toolbar);

      /*  String[] buss_name = new String[] {"Test Bussines 1","Test Bussines 2"};
        String[] mobile = new String[] {"03201234556","03214567891"};
        String[] adress = new String[] {"Test Adress 1","Test Adress 2"};
*/
        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Prefs", MODE_PRIVATE);

        saleman_id=  pref.getString("id", null);
        saleman_name = pref.getString("salesman_name",null);
        isFirst = pref.getString("isFirst","");


        Intent intent = getIntent();
        heading = intent.getStringExtra("from");

        listView = (ListView) findViewById(R.id.customer_list);
        addcustomer = (ImageView) findViewById(R.id.addcustomer);
        searchEDt = (EditText) findViewById(R.id.search);
        customer_addapter = new Customer_Addapter(Customers.this, R.layout.row_customer, list, Customers.this);

        searchEDt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchEDt.getText().toString().toLowerCase(Locale.getDefault());


                    customer_addapter.filter(text);


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
       // NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

        Boolean check = isNetworkAvailable();

        if (check.equals(true) && isFirst.equals("Done"))
        {
             //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
            // actionBar.setTitle(getString(R.string.app_name) + "             " + "Connection UP");




            String amount , remaining , bussid;
            Cursor cus =database.showRecovry();

            if (cus.moveToFirst()) {
                do {
                    bussid=  cus.getString(1);
                    amount=  cus.getString(2);
                    remaining=  cus.getString(3);

                    sendRecovryLocal(amount,remaining,bussid);
                } while (cus.moveToNext());
            }


           List = database.getAllSales();
            if (List.equals("") || List.size() == 0) {

            } else {

              /*  ringProgressDialog = ProgressDialog.show(this, "please wait", "Adding To Server From Local DataBase", true);
                ringProgressDialog.setCancelable(false);
                ringProgressDialog.show();*/

                for(int i=0;i<List.size();i++)
                {
                    EnterSales(List.get(i));
                }

               /* ringProgressDialog.dismiss();*/




            }

        }
        else
        {
           // Toast.makeText(Customers.this, "Not Connected", Toast.LENGTH_SHORT).show();
        }






        getCustomer();

        addcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Customers.this,Add_New_Customer.class);
                startActivity(intent);
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String buss_id = list.get(position).getId();


                SharedPreferences pref = getApplicationContext().getSharedPreferences("Buss_details", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("b_name",list.get(position).b_name);
                editor.putString("b_personal",list.get(position).peronal_name);
                editor.putString("b_mobile",list.get(position).mobile);
                editor.putString("salesman_name",saleman_name);
                editor.putString("adress",list.get(position).getAdress());

                editor.commit();





                if(heading.equals("sale"))
                {
                    Intent intent= new Intent(Customers.this,Sales.class);
                    intent.putExtra("buss_id", buss_id);
                    finish();
                    startActivity(intent);
                }

                if(heading.equals("recovry"))
                {
                    Intent intent= new Intent(Customers.this,Recovery.class);
                    intent.putExtra("buss_id", buss_id);
                    finish();
                    startActivity(intent);
                }

                if (heading.equals("sample"))
                {
                    Intent intent= new Intent(Customers.this,Sample.class);
                    intent.putExtra("buss_id",buss_id);
                    finish();
                    startActivity(intent);
                }

                if (heading.equals("supply"))
                {
                    Intent intent= new Intent(Customers.this,Supply.class);
                    intent.putExtra("buss_id",buss_id);
                    finish();
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

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("isFirst","Done");
                        editor.apply();



                        parseJSONResponce(response);

                        addCustomerToLocal();

                        listView.setAdapter(customer_addapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();

                if (error instanceof NoConnectionError)
                {
                   loadFromDB();
                }
                else if (error instanceof TimeoutError)
                {
                    loadFromDB();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",saleman_id);
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

    public void addCustomerToLocal()
    {

        database.clearTable("customers");

        for (int i = 0; i < list.size(); i++) {

            String id= list.get(i).getId();
            String bussines_name = list.get(i).getB_name();
            String  personal_name= list.get(i).getPeronal_name();
            String address =  list.get(i).getAdress();
            String mobile = list.get(i).getMobile();
            String district = list.get(i).getDistrcit();

           database.insertBussines(bussines_name,id,personal_name,address,mobile,district,saleman_id);

        }
    }

    public void loadFromDB()
    {
        Cursor cus =  database.getData("customers");

        if (cus.getCount() == 0)
        {
            return;
        }

        while (cus.moveToNext())
        {
            String id =cus.getString(0);
            String bussines_name = cus.getString(1);
            String personal_name =cus.getString(2);
            String address = cus.getString(3);
            String distrcit = cus.getString(5);
            String mobile = cus.getString(4);

            Customer_model data = new Customer_model();
            data.setAdress(address);
            data.setB_name(bussines_name);
            data.setDistrcit(distrcit);
            data.setMobile(mobile);
            data.setPeronal_name(personal_name);
            data.setId(id);

            list.add(data);

        }

        listView.setAdapter(customer_addapter);
    }



    public void sendRecovryLocal(final String amount, final String remaining , final String bussines ) {

        /*ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();*/

        String URL =null;

        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.SEND_RECOVRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                      //  ringProgressDialog.dismiss();

                        database.updatestatus(bussines);
                        Toast.makeText(Customers.this, "Recovery added succesfully", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               // ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError)
                {

                    Toast.makeText(Customers.this, "Net disconnected !!", Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recovry",amount);
                params.put("remaining",remaining);
                params.put("buss_id",bussines);


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








    public void EnterSales(final String sale) {



        String URL =null;

        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.BASE_URL+"Welcome/sale",
                new Response.Listener<String>() {
                    @Override
                        public void onResponse(String response) {

                      //  ringProgressDialog.dismiss();

                        Toast.makeText(Customers.this, "Entered from local data base ", Toast.LENGTH_SHORT).show();

                        database.clearTable("Sales");
                        List.clear();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                 if (error instanceof TimeoutError) {

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(Customers.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("sales",sale);

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


        new AlertDialog.Builder(this)
                .setTitle("Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                }).create().show();


    }
}


