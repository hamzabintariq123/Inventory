package com.hamza.inventory.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hamza.inventory.Date_Models.Customer_model;
import com.hamza.inventory.Date_Models.Parcel_model;
import com.hamza.inventory.R;

import java.util.ArrayList;

/**
 * Created by Hamza Android on 11/10/2016.
 */
public class Parcel_Addapter extends ArrayAdapter<String>

{

    public ArrayList<Parcel_model> list ;


    Activity activity;

    public Parcel_Addapter(Context context, int resource, ArrayList<Parcel_model> list, Activity activity) {
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
        rowView = inflater.inflate(R.layout.row_parcel,null,true);

        TextView date = (TextView) rowView.findViewById(R.id.parcel_date);
        TextView name = (TextView) rowView.findViewById(R.id.name);



        Parcel_model  model  = list.get(position);


        date.setText(model.getProduct_date());
        name.setText(model.getProduct());

        return rowView;
    }
}
