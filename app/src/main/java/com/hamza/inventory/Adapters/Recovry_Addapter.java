package com.hamza.inventory.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hamza.inventory.R;


public class Recovry_Addapter  extends ArrayAdapter<String>

{
    String product[],quantity[];

    Activity context;



    public Recovry_Addapter(Activity context, String[] name , String[] adress)
    {
        super(context, R.layout.row_customer,name);
        this.context=context;
        this.product = name;
        this.quantity = adress;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View rowView;


        LayoutInflater inflater = context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.row_recovry,null,true);

        TextView Product = (TextView) rowView.findViewById(R.id.pro_rec);
        TextView Quantity = (TextView) rowView.findViewById(R.id.qua_rec);


        Product.setText(product[position]);
        Quantity.setText(quantity[position]);




        return rowView;
    }

}
