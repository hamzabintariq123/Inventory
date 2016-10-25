package com.hamza.inventory.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hamza.inventory.Activities.Customers;
import com.hamza.inventory.Date_Models.Customer_model;
import com.hamza.inventory.R;

import java.util.ArrayList;

/**
 * Created by Harry on 9/30/2016.
 */
public class Customer_Addapter extends ArrayAdapter<String>

{

    public ArrayList<Customer_model> list ;


    Activity activity;

    public Customer_Addapter(Context context, int resource, ArrayList<Customer_model> list, Activity activity) {
        super(context, resource);
        this.list = list;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View rowView;


        LayoutInflater inflater = activity.getLayoutInflater();
        rowView = inflater.inflate(R.layout.row_customer,null,true);

        TextView Name = (TextView) rowView.findViewById(R.id.breakfast);
        TextView Addres = (TextView) rowView.findViewById(R.id.dinnet);
        TextView Number = (TextView) rowView.findViewById(R.id.fuel);


        Customer_model  model  = list.get(position);


        Name.setText(model.getB_name());
        Addres.setText(model.getAdress());
        Number.setText(model.getMobile());



        return rowView;
    }
}

