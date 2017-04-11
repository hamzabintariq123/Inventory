package com.hamza.inventory.Adapters;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class Printer_addapter {

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mBluetoothDevice=null;
    private BluetoothSocket mBluetoothSocket=null;
    OutputStream mOutputStream=null;


    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    String line = "===============================================\n";


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

            String b_name="",b_personal="",b_mobile="",b_saleman_nmae="",b_total="",b_amount="",b_paid="";
          SharedPreferences pref = context.getApplicationContext().getSharedPreferences("Buss_details", context.MODE_PRIVATE);
            b_name  = pref.getString("b_name",null);
            b_personal =pref.getString("b_personal",null);
            b_mobile  =pref.getString("b_mobile",null);
            b_saleman_nmae= pref.getString("salesman_name",null);
            b_total= pref.getString("total",null);
            b_amount= pref.getString("remaining",null);
            b_paid= pref.getString("paid",null);


            mOutputStream=mBluetoothSocket.getOutputStream();


            String header = "NAME              PRICE     QUANTITY    TOTAL\n";
            String date = getCurrDateTime()+"\n"+"   "+LOGO+"\n"+line;


            String[] info = new String[]{b_name,"\n"+date+"\n",
                   " Personal name  "+b_personal+"\n"
            +" Mobile number  "+b_mobile+"\n"+" Saleman Name   "+b_saleman_nmae+"\n",header+"\n"+line};

            for(int i =0;i <info.length ;i++)
            {

                mOutputStream.write(info[i].getBytes("GBK"));
                mOutputStream.flush();

            }



            ArrayList<HashMap<String,String>> formatedData = formatData(toPrint);
            ArrayList<String> finalData = getDataToPrint(formatedData);

            for(int i =0;i <finalData.size() ;i++)
            {

                mOutputStream.write((finalData.get(i).toString()).getBytes("GBK"));
                mOutputStream.flush();

            }

            String Record =  "                   Paid = "+b_paid+"\n"
                            +"                   Remaining = "+b_amount+"\n"
                            +"                   Total = " +b_total+"\n"
                            +line+"\n";

            mOutputStream.write(Record.getBytes("GBK"));
            mOutputStream.flush();


            String signature = "Sales Man = _____________________ . \n\n\n\n";

            mOutputStream.write(signature.getBytes("GBK"));
            mOutputStream.flush();

            String PrintFotter = "";

            if(LOGO.equals("Neelam Labs"))
            {
                PrintFotter = "“I hereby declare that goods manufactured & sold by Neelam Laboratories and Dawakhana (PVT)Ltd. are pure Unani products and are prepared strictly to the Unani and Ayuorvadic system of Medicine. We have been enlisted according to new Drug Regulatory Authority of Pakistan (DRAP) Act 2012.”";
            }
            else if (LOGO.equals("Nature's Home Registered"))
            {
                PrintFotter = "“I hereby declare that goods manufactured & sold by Nature’s Home Registered Lahore are pure Unani products and are prepared strictly to the Unani and Ayuorvadic system of Medicine. We have been enlisted according to new Drug Regulatory Authority of Pakistan (DRAP) Act 2012.”";
            }
            else if(LOGO.equals("Neelam Labs& Nature's Home Registered"))
            {
                PrintFotter = "“I hereby declare that goods manufactured & sold by Nature’s Home Registered Lahore & Neelam Laboratories and Dawakhana (PVT)Ltd  are pure Unani products and are prepared strictly to the Unani and Ayuorvadic system of Medicine. We have been enlisted according to new Drug Regulatory Authority of Pakistan (DRAP) Act 2012.”";
            }

            mOutputStream.write(PrintFotter.getBytes("GBK"));
            mOutputStream.flush();

            Log.d("sent", "send sucessfully");

        } catch (Exception e) {
            // TODO Auto=generated catch block
            e.printStackTrace();
            Toast.makeText(context, "Exception in Printing data", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<HashMap<String,String>> formatData(String data)
    {

        HashMap<String,String> valueMap = new HashMap<>();
        ArrayList<HashMap<String,String>> record = new ArrayList<>();
        String splitter = "\\{";
        String[] row = data.split(splitter);

        int j =0;

        for(int i=0;i<row.length;i++)
        {
            if (i==0) {

            }
            else
            {


                String[] singleItem = row[i].split(",");

                valueMap.put("productName"+j, singleItem[2]);
                valueMap.put("productPrice"+j, singleItem[3]);
                valueMap.put("productQuantity"+j, singleItem[4]);
                valueMap.put("total"+j, singleItem[7]);

                record.add(valueMap);

                j++;
            }

        }


        return record;
    }

    private ArrayList<String> getDataToPrint(ArrayList<HashMap<String, String>> formatedData)
    {

        ArrayList<String> data = new ArrayList<>();

        for(int i =0;i<formatedData.size();i++)
        {
            String name = formatedData.get(i).get("productName"+i);



                data.add(formatedData.get(i).get("productName"+i)+"   ");
                data.add(formatedData.get(i).get("productPrice"+i) + "         ");
                data.add(formatedData.get(i).get("productQuantity"+i) + "        ");
                data.add(formatedData.get(i).get("total"+i)+"\n");



            data.add(line);
        }




        return data;
    }

    private String getCurrDateTime() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;

    }


}
