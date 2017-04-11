package com.hamza.inventory.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.hamza.inventory.Adapters.Product_Addapter;
import com.hamza.inventory.Date_Models.Sale_model;
import com.hamza.inventory.Network.EndPoints;
import com.hamza.inventory.R;
import com.hamza.inventory.SQLite_DB.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Sales extends AppCompatActivity {


    Toolbar toolbar;
    Button send;
    ImageView add;
    ListView product_list;
    TextView total;
    SQLiteDatabase db;
    Database database = new Database(this);
    String Sales = "",user_id,strbuss_id,ids;
    float total_am;
    JSONObject jObjSaleModel =  new JSONObject();
    Product_Addapter product_addapter = null;
    Sale_model objSaleModel = new Sale_model();
    static ArrayList<Sale_model> arrSaleData = new ArrayList<>();
    static JSONArray arrJsonSaleData = new JSONArray();
    ProgressDialog ringProgressDialog;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);


        try {
            database=database.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Sales");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i = getIntent();
        int rate = i.getIntExtra("rate", 0);
        int quantity = i.getIntExtra("quantity",0);
        String strProduct = i.getStringExtra("productName");
        String strTotal = i.getStringExtra("Rotal");
        String strdiscount = i .getStringExtra("discount");
        strbuss_id = i .getStringExtra("buss_id");

        add = (ImageView) findViewById(R.id.add);
        send = (Button) findViewById(R.id.send_rec);
        product_list = (ListView) findViewById(R.id.sample_list);
        total = (TextView) findViewById(R.id.total_amount);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Prefs", MODE_PRIVATE);
        user_id = pref.getString("id",null);

        if(i.hasExtra("rate"))
        {

            objSaleModel.setProductName(strProduct);
            objSaleModel.setProductRate(String.valueOf(rate));
            objSaleModel.setProductAmount(String.valueOf(strTotal));
            objSaleModel.setProductQuantity(String.valueOf(quantity));
            objSaleModel.setDiscount(strdiscount);


            try {
                jObjSaleModel.put("product_name" , strProduct);
                jObjSaleModel.put("product_rate" , String.valueOf(rate));
                jObjSaleModel.put("product_amount" , String.valueOf(strTotal));
                jObjSaleModel.put("product_quantity" , String.valueOf(quantity));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            arrSaleData.add(objSaleModel);
            arrJsonSaleData.put(jObjSaleModel);


            for (int j=0;j<arrSaleData.size();j++)
            {
                total_am = total_am+Float.parseFloat(arrSaleData.get(j).getProductAmount());

            }

            total.setText(total_am + "");

       /* String[] product = new String[] {"Product 1","Product 2"};
        String[] quantity = new String[] {"50","40"};
        String[] rate = new String[] {"400","250"};
        String[] amount = new String[] {"20000","10000"};
*/
            product_addapter = new Product_Addapter(com.hamza.inventory.Activities.Sales.this,arrSaleData);

            product_list.setAdapter(product_addapter);
        }






        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(com.hamza.inventory.Activities.Sales.this, Add_Products.class);
                intent.putExtra("buss_id",strbuss_id);
                intent.putExtra("from","products");
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                for (int k =0 ; k<arrSaleData.size();k++)
                {
                    Sales= Sales+"{"+user_id+","+strbuss_id+","+arrSaleData.get(k).getProductName()+","+arrSaleData.get(k).getProductRate()+","+
                            arrSaleData.get(k).getProductQuantity()+","+arrSaleData.get(k).getDiscount()+","+"Sale"+","+
                            arrSaleData.get(k).getProductAmount()+","+formattedDate;

                }

                if(Sales.equals("") || Sales ==  null)
                {
                    Toast.makeText(Sales.this, "Please Enter Some records", Toast.LENGTH_SHORT).show();
                }

                else
                {

                        arrSaleData.clear();

                        Intent intent = new Intent(com.hamza.inventory.Activities.Sales.this, Payment.class);
                        intent.putExtra("sales",Sales);
                        intent.putExtra("total_amount",total_am);
                        startActivity(intent);





                       // EnterSales();

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("Buss_details", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("total",total_am+"");
                        editor.putString("remaining",total_am+"");
                        editor.putString("paid","0");
                        editor.commit();

                        // Intent intent = new Intent(Sales.this, Customers.class);
                        //startActivity(intent);



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
        arrSaleData.clear();
        Intent intent= new Intent(com.hamza.inventory.Activities.Sales.this, Customers.class);
        intent.putExtra("from","sale");
        startActivity(intent);
        finish();
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

                        arrSaleData.clear();


                        ringProgressDialog.dismiss();

                        if(response.equals(""))
                        {
                            Toast.makeText(Sales.this, "Records not entered ! Some thing went wrong", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        else
                        {
                            Toast.makeText(Sales.this, "Entered Sucessfully", Toast.LENGTH_SHORT).show();

                           /* if (checkBox.isChecked()) {*/

                                Intent intent = new Intent(com.hamza.inventory.Activities.Sales.this, Payment.class);
                                intent.putExtra("sales",Sales);
                                intent.putExtra("colums",response);
                                intent.putExtra("total_amount",total_am);
                                startActivity(intent);


                          /*  }
                            else
                            {

                                Intent intent = new Intent(Sales.this, Printer.class);
                                 intent.putExtra("sale",Sales);
                                startActivity(intent);
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


                    Toast.makeText(com.hamza.inventory.Activities.Sales.this, "No Internet Connection !! Adding to local DataBase", Toast.LENGTH_SHORT).show();

                    database.insertSales(Sales);



                    Intent intent = new Intent(Sales.this, Printer.class);
                    intent.putExtra("sale", Sales);
                    startActivity(intent);

                    // Intent intent = new Intent(Sales.this, Printer.class);
                    // intent.putExtra("sale",Sales);
                    // startActivity(intent);

                } else if (error instanceof TimeoutError) {
                    ringProgressDialog.dismiss();
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(com.hamza.inventory.Activities.Sales.this, message, Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(com.hamza.inventory.Activities.Sales.this,Customers.class);
        intent.putExtra("from","sale");
        startActivity(intent);

    }
}
