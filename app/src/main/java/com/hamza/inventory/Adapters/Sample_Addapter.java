package com.hamza.inventory.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hamza.inventory.R;

/**
 * Created by Harry on 10/5/2016.
 */
public class Sample_Addapter  extends ArrayAdapter<String>

{
    String product[],rate[],quantity[];

    Activity context;



    public Sample_Addapter(Activity context, String[] name , String[] adress ,String [] mobile )
    {
        super(context, R.layout.row_customer,name);
        this.context=context;
        this.product = name;
        this.rate = adress;
        this.quantity = mobile;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View rowView;


        LayoutInflater inflater = context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.row_sample,null,true);

        TextView Product = (TextView) rowView.findViewById(R.id.produt);
        TextView Rate = (TextView) rowView.findViewById(R.id.rate_sam);
        TextView Quantity = (TextView) rowView.findViewById(R.id.quantity_sam);


        Product.setText(product[position]);
        Rate.setText(rate[position]);
        Quantity.setText(quantity[position]);




        return rowView;
    }
}

