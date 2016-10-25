package com.hamza.inventory.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hamza.inventory.Date_Models.Sale_model;
import com.hamza.inventory.R;

import java.util.ArrayList;


public class Product_Addapter extends BaseAdapter

{
    ArrayList<Sale_model> arrSaleData;

    Activity context;



    public Product_Addapter(Activity context, ArrayList<Sale_model> arrSaleData)
    {
        this.context=context;
        this.arrSaleData = arrSaleData;
    }

    @Override
    public int getCount() {
        return arrSaleData.size();
    }

    @Override
    public Object getItem(int position) {
        return arrSaleData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

        Product.setText(arrSaleData.get(position).getProductName());
        Rate.setText(arrSaleData.get(position).getProductRate());
        Quantity.setText(arrSaleData.get(position).getProductQuantity());
        Amount.setText(arrSaleData.get(position).getProductAmount());



        return rowView;
    }
}
