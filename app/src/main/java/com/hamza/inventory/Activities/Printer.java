package com.hamza.inventory.Activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.hamza.inventory.Adapters.Printer_addapter;
import com.hamza.inventory.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Printer extends AppCompatActivity {

    Spinner logo,device;
    Button print,Connect;
    private List<String> mpairedDeviceList=new ArrayList<String>();
    private ArrayAdapter<String> mArrayAdapter;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mBluetoothDevice=null;
    Set<BluetoothDevice> pairedDevices=null;
    private AlertDialog.Builder dialog=null;
    Printer_addapter printer_addapter;
    private List<String> mlogoAddapter=new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;
    String sale;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);

        logo = (Spinner) findViewById(R.id.logo_spinner);
        device = (Spinner) findViewById(R.id.device_spinner);
        print  = (Button) findViewById(R.id.print_button);
        Connect = (Button) findViewById(R.id.connect);

        Intent intent = getIntent();
        sale = intent.getStringExtra("sale");

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Payment");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        dialog=new AlertDialog.Builder(this);
        dialog.setTitle("posPrinter hint:");
        dialog.setMessage("Bluetooth was not opened,or not paired device.\\nOpen bluetooth now ?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                //finish();
            }
        });

        dialog.setNeutralButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                //finish();
            }
        });




        printer_addapter = new Printer_addapter();
        // setting logo spiiner
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        mlogoAddapter.add("Neelam Labs");
        mlogoAddapter.add("Nature's Home Registered");
        mlogoAddapter.add("Neelam Labs& Nature's Home Registered");
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mlogoAddapter);
        logo.setAdapter(mAdapter);



       Connect.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String responce = "";
               if(Connect.getText().toString().equals("Connect"))
               {
                   responce = printer_addapter.connectDevices(device.getSelectedItem().toString(),"Connect",Printer.this);

                   if(responce .equals("1"))
                   {
                       Toast.makeText(Printer.this, "Connected Succesfully", Toast.LENGTH_SHORT).show();
                       Connect.setText("Dis-Connect");

                   }
                   else

                   {
                       Toast.makeText(Printer.this, "Error While Connecting", Toast.LENGTH_SHORT).show();
                   }

               }

               else
               {
                   responce = printer_addapter.connectDevices(device.getSelectedItem().toString(),"Dis-Connect",Printer.this);

                   if(responce .equals("2"))
                   {
                       Toast.makeText(Printer.this, "Disconnected  Succesfully", Toast.LENGTH_SHORT).show();
                       Connect.setText("Connect");
                   }
                   else
                   {
                       Toast.makeText(Printer.this, "Error While Dis-Connecting", Toast.LENGTH_SHORT).show();
                   }

               }



           }
       });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(device.getSelectedItem().toString().equals("Please Select the Bluetooth Printer"))
                {

                    Toast.makeText(Printer.this, "You have not selected a bluetooth device", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    String strlogo =  logo.getSelectedItem().toString();

                    printer_addapter.printData(strlogo,sale,Printer.this);


                }

            }
        });



        mpairedDeviceList.add("Please Select the Bluetooth Printer");
        mArrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,mpairedDeviceList);
        device.setAdapter(mArrayAdapter);
        device.setOnTouchListener(new Spinner.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction()!=MotionEvent.ACTION_UP) {
                    return false;
                }
                try {
                    if (mBluetoothAdapter==null) {

                        Toast.makeText(Printer.this, "No Bluetooth Adapter Found", Toast.LENGTH_SHORT).show();
                        dialog.create().show();
                    }
                    else if (mBluetoothAdapter.isEnabled()) {
                        String getName=mBluetoothAdapter.getName();
                        pairedDevices=mBluetoothAdapter.getBondedDevices();
                        while (mpairedDeviceList.size()>1) {
                            mpairedDeviceList.remove(1);
                        }
                        if (pairedDevices.size()==0) {
                           dialog.create().show();
                        }
                        for (BluetoothDevice device : pairedDevices) {
                            // Add the name and address to an array adapter to show in a ListView
                            getName=device.getName() + "#" + device.getAddress();
                            mpairedDeviceList.add(getName);
                        }
                    }
                    else {
                        Toast.makeText(Printer.this, "BluetoothAdapter not open...", Toast.LENGTH_SHORT).show();
                        dialog.create().show();
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                    Toast.makeText(Printer.this,e.toString(), Toast.LENGTH_SHORT).show();;
                }
                return false;
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
        Intent intent= new Intent(Printer.this, Customers.class);
        intent.putExtra("from","sale");
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(Printer.this, Customers.class);
        intent.putExtra("from","sale");
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}






