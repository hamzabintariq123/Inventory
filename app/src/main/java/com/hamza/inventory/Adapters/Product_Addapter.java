package com.hamza.inventory.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hamza.inventory.R;


public class Product_Addapter  extends ArrayAdapter<String>

{
    String product[],rate[],quantity[],amount[];

    Activity context;



    public Product_Addapter(Activity context, String[] name , String[] adress ,String [] mobile ,String [] amount)
    {
        super(context, R.layout.row_customer,name);
        this.context=context;
        this.product = name;
        this.rate = adress;
        this.quantity = mobile;
        this.amount=amount;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View rowView;


        LayoutInflater inflater = context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.row_product,null,true);

        TextView Product = (TextView) rowView.findViewById(R.id.products);
        TextView Rate = (TextView) rowView.findViewById(R.id.rate);
        TextView Quantity = (TextView) rowView.findViewById(R.id.quantity);
        TextView Amount = (TextView) rowView.findViewById(R.id.amount);

        Product.setText(product[position]);
        Rate.setText(rate[position]);
        Quantity.setText(quantity[position]);
        Amount.setText(amount[position]);



        return rowView;
    }
}
