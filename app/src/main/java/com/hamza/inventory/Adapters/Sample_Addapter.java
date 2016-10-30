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

/**
 * Created by Harry on 10/5/2016.
 */
public class Sample_Addapter  extends BaseAdapter
{
    ArrayList<Sale_model> arrSaleData;

    Activity context;



    public Sample_Addapter(Activity context, ArrayList<Sale_model> arrSaleData)
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
        rowView = inflater.inflate(R.layout.row_sample,null,true);

        TextView Product = (TextView) rowView.findViewById(R.id.produt);
        TextView Rate = (TextView) rowView.findViewById(R.id.rate_sam);
        TextView Quantity = (TextView) rowView.findViewById(R.id.quantity_sam);

        Product.setText(arrSaleData.get(position).getProductName());
        Rate.setText(arrSaleData.get(position).getProductRate());
        Quantity.setText(arrSaleData.get(position).getProductQuantity());

        return rowView;

    }
}

