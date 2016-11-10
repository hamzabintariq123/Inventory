package com.hamza.inventory.Activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hamza.inventory.Fragments.DrawerFragment;
import com.hamza.inventory.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    DrawerFragment drawerFragment = new DrawerFragment();
    Button existingCustomer,newCustomer;
    TextView from;
    String heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("New Sales");

        heading = "";

        Intent intent = getIntent();
       heading = intent.getStringExtra("from");

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        drawerFragment.setup((DrawerLayout) findViewById(R.id.drawerlayout), toolbar);

        existingCustomer = (Button) findViewById(R.id.ret_customer);
        newCustomer = (Button) findViewById(R.id.quantty);
        from = (TextView) findViewById(R.id.from_screen);

        if(heading == null)
            {
                heading = "sale";
                from.setText(heading.toUpperCase()+ " SCREEN");
            }
        else
        {

            from.setText(heading.toUpperCase()+" SCREEN");

        }



            existingCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent  intent= new Intent(MainActivity.this,Customers.class);
                    intent.putExtra("from",heading);
                    startActivity(intent);

                }
            });

            newCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, Add_New_Customer.class);
                    intent.putExtra("from",heading);
                    startActivity(intent);
                }
            });




    }


    @Override
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}
