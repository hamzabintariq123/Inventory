package com.hamza.inventory.Fragments;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hamza.inventory.Activities.Customers;
import com.hamza.inventory.Activities.Expence;
import com.hamza.inventory.Activities.Login;
import com.hamza.inventory.Activities.MainActivity;
import com.hamza.inventory.Activities.Parcel;
import com.hamza.inventory.Activities.Recovery;
import com.hamza.inventory.Activities.Supply;
import com.hamza.inventory.Adapters.Drawer_Addapter;
import com.hamza.inventory.R;


public class DrawerFragment extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;


    Drawer_Addapter drawer_addapter = null;

    String[] nameArray = new String[] {"Sale","Recovery","Sample","Supply","Expense","Parcel","Logout"};
    int[] images =  new int[] {R.drawable.sales,R.drawable.recovry,R.drawable.sample,R.drawable.supply,
    R.drawable.expense,R.drawable.parcel,R.drawable.logout};
    ListView listView;

    public DrawerFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_drawer, container, false);
        v.setClickable(true);

        listView = (ListView) v.findViewById(R.id.listView1);

        drawer_addapter = new Drawer_Addapter(getActivity(),nameArray,images);

        listView.setAdapter(drawer_addapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                if (position == 0 )
                {
                    Intent intent = new Intent(getActivity(), Customers.class);
                    intent.putExtra("from","sale");
                    getActivity().startActivity(intent);
                    getActivity().finish();



                }
                if (position == 1 )
                {
                    Intent intent = new Intent(getActivity(), Customers.class);
                    intent.putExtra("from", "recovry");
                    getActivity().startActivity(intent);



                }


                if (position == 2 )
                {
                    Intent intent = new Intent(getActivity(), Customers.class);

                    getActivity().startActivity(intent);
                }

                if (position == 3 )
                {
                    Intent intent = new Intent(getActivity(), Customers.class);
                    intent.putExtra("from","supply");
                    getActivity().startActivity(intent);

                }
                if (position == 4 )
                {
                    Intent intent = new Intent(getActivity(), Expence.class);
                    getActivity().startActivity(intent);


                }

                if (position == 5)
                {

                    Intent intent = new Intent(getActivity(), Parcel.class);
                    getActivity().startActivity(intent);

                }
                if (position == 6 )
                {

                    SharedPreferences pref = getActivity().getSharedPreferences("User Prefs", getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();

                    Intent i = new Intent(getActivity(), Login.class);

                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                }

            }
        });

        return v;
    }
    public void setup(DrawerLayout dawerLayout ,Toolbar toolbar)


    {


        mDrawerLayout = dawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.draweropem, R.string.drawerclose) {
            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);



                // getActivity().supportInvalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
                //  getActivity().supportInvalidateOptionsMenu();


            }
        };



    }

}
