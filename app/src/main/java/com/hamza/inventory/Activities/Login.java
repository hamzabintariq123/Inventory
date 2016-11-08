package com.hamza.inventory.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {


    Button  submit;
    EditText username,password;
    String name,pass;
    CheckBox loggedin;
    String id,district;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {



        SharedPreferences pref = getApplicationContext().getSharedPreferences("User Prefs", MODE_PRIVATE);
        String user_session= pref.getString("remmember_me", null);


        if(user_session != null)
        {
            Intent intent= new Intent(Login.this, Customers.class);
            intent.putExtra("from","sale");
            startActivity(intent);
            finish();

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loggedin = (CheckBox) findViewById(R.id.checkBox);
        submit = (Button) findViewById(R.id.submit);
        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getValues();
                if(name.equals("")||pass.equals(""))
                {
                    Toast.makeText(Login.this, "Enter User Name And Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loginUser();
                }


            }
        });


    }
      public void  getValues()
      {
          name = username.getText().toString();
          pass = password.getText().toString();

      }

    public void loginUser() {

        ringProgressDialog = ProgressDialog.show(this, "", "please wait", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();



        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();

                        if (response.equals("false")) {
                            Toast.makeText(Login.this, "Please Enter Correct User Name and Password", Toast.LENGTH_SHORT).show();

                        } else {


                            try {

                                JSONObject object = new JSONObject(response);


                                id = object.getString("id");
                                district =object.getString("area_of_supply");


                                SharedPreferences pref = getApplicationContext().getSharedPreferences("User Prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();

                                editor.putString("id", id);// Saving string
                                editor.putString("district", district);



                                if (loggedin.isChecked()) {

                                    editor.putString("remmember_me", "loggedin");

                                }

                                editor.commit();

                                Intent intent= new Intent(Login.this, Customers.class);
                                intent.putExtra("from","sale");
                                startActivity(intent);
                                finish();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError)
                {
                    Toast.makeText(Login.this,"No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else if (error instanceof TimeoutError) {


                    Toast.makeText(Login.this, "Connection TimeOut! Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("user_name", "hamza");
                params.put("password", "1234");

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
