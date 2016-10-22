package com.hamza.inventory.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hamza.inventory.Date_Models.Customer_model;
import com.hamza.inventory.R;

import java.util.ArrayList;

/**
 * Created by Harry on 9/30/2016.
 */
public class Customer_Addapter extends ArrayAdapter<String>

{

    public ArrayList<Customer_model> list;
    Activity context;



    public Customer_Addapter(Activity context, ArrayList<Customer_model> list)
    {
        super(context, R.layout.row_customer);
        this.context=context;
        this.list = list;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View rowView;


        LayoutInflater inflater = context.getLayoutInflater();
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

