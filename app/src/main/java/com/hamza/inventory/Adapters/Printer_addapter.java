package com.hamza.inventory.Adapters;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hamza.inventory.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class Printer_addapter {

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mBluetoothDevice=null;
    private BluetoothSocket mBluetoothSocket=null;
    OutputStream mOutputStream=null;


    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");




    public String  connectDevices(String deviceName,String Action,Context context) {

        String responce= "";
        if (Action.equals("Connect")) {

            if (deviceName.equals("")) {
                Toast.makeText(context, "Plesae select a bluetooth device...", Toast.LENGTH_SHORT).show();
            } else {


                deviceName = deviceName.substring(deviceName.length() - 17);
                try {
                    mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
                    mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(deviceName);
                    mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID);
                    mBluetoothSocket.connect();
                    Toast.makeText(context, "Connected Succesfully to "+deviceName, Toast.LENGTH_SHORT).show();
                    responce= "1";

                } catch (Exception e) {
                    Toast.makeText(context, "Error in Connecting To Bluetooth Device...", Toast.LENGTH_SHORT).show();

                }

            }
        }
        else
        {
            try {
                mOutputStream.close();
                mBluetoothSocket.close();
                Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
                responce= "2";
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(context, "Disconnection Error", Toast.LENGTH_SHORT).show();
            }
        }

        return responce;
    }


    public void printData(String LOGO ,String toPrint,Context context)
    {
        try {
            if (toPrint.equals("")) {
                Toast.makeText(context, "Plesae Enter Sone Records", Toast.LENGTH_SHORT).show();
            }

            mOutputStream=mBluetoothSocket.getOutputStream();
            mOutputStream.write(("                     " + LOGO + "\n"


            ).getBytes("GBK"));
            mOutputStream.flush();
            Log.d("sent","send sucessfully");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, "Exception in Printing data", Toast.LENGTH_SHORT).show();
        }
    }


}
