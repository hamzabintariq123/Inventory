package com.hamza.inventory.SQLite_DB;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hamza.inventory.Date_Models.Customer_model;
import com.hamza.inventory.Date_Models.Products_model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Database {

    static final String DATABASE_NAME = "Neelam_Local.db";
    static final int DATABASE_VERSION = 1;

    private static final String PRODUCTS_TABLE = "Products";
    static final String DATABASE_CREATE = "create table " + "Products" + "( "
            + "ProductID" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "Productname  varchar, Quantity INTEGER , Trade_Price INTEGER , Retail_Price INTEGER);); ";

    private static final String ACCOUNTS_TABLE = "Sales";
    static final String DATABASE_ACCOUNTS = "create table " + "Sales" + "( "
            + "SalesID" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "productName  varchar, salesman_id INTEGER , bussines_id INTEGER ,product_price INTEGER ," +
            "quantity INTEGER ,discount INTEGER , total INTEGER ,type varhar);); ";

    private static final String CUSTOMERS_TABLE = "customers";
    static final String DATABASE_CUSTOMER = "create table " + "customers" + "( "
            + "CustomerID" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "bussiness_name  varchar, personal_name varchar , address varchar ,distrcit varchar ," +
            "mobile INTEGER ,salesman INTEGER );); ";



    public static SQLiteDatabase db;
    private final Context context;
    private DataBaseHelper dbHelper;


    public Database(Context _context) {

        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    public Database open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void insertProduct(String Productname, Integer qauntity ,Integer T_price ,Integer R_price )
            {
                    ContentValues newValues = new ContentValues();
                    newValues.put("Productname", Productname);
                    newValues.put("Quantity", qauntity);
                    newValues.put("Trade_Price", T_price);
                    newValues.put("Retail_Price", R_price);
                    db.insert(PRODUCTS_TABLE, null, newValues);



             }

    public void insertBussines(String bussines_name, String personal_name ,String address ,String mobile,String distrcit )
    {
        ContentValues newValues = new ContentValues();
        newValues.put("bussiness_name", bussines_name);
        newValues.put("personal_name", personal_name);
        newValues.put("address", address);
        newValues.put("mobile", mobile);
        newValues.put("distrcit", distrcit);
        long test = db.insert(CUSTOMERS_TABLE, null, newValues);
    }



    public void clearTable(String TABLE_NAME)
    {
            dbHelper.getWritableDatabase();
            db.execSQL("delete from "+ TABLE_NAME );

    }



    public ArrayList<Products_model> getAllProductss() {
        ArrayList<Products_model> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + PRODUCTS_TABLE;

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Products_model products_model = new Products_model();
                products_model.setId(cursor.getString(0));
                products_model.setName(cursor.getString(1));
                products_model.setQuantay(cursor.getString(2));
                products_model.setTrade(cursor.getString(3));
                products_model.setRetail(cursor.getString(4));
                // Adding contact to list
                contactList.add(products_model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public ArrayList<Customer_model> getAllCustomers() {
        ArrayList<Customer_model> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CUSTOMERS_TABLE;

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer_model Customer_model = new Customer_model();
                Customer_model.setId(cursor.getString(0));
                Customer_model.setB_name(cursor.getString(1));
                Customer_model.setPeronal_name(cursor.getString(2));
                Customer_model.setDistrcit(cursor.getString(3));
                Customer_model.setAdress(cursor.getString(4));
                Customer_model.setMobile(cursor.getString(5));
                Customer_model.setMobile(cursor.getString(6));
                Customer_model.setSalesman(cursor.getString(7));
                // Adding contact to list
                contactList.add(Customer_model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    public Integer insertSales(Integer salesman_id, Integer bussines_id, Integer product_price, String 	Date_added,
                               String product_name,Integer quantity,Integer discount,String type,Integer total) {

      /*  Cursor mCursor = db.rawQuery("SELECT Quantity FROM " + "Products" + " WHERE  Productname=?", new String[]{product_name});
        if (mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            int entry;
            entry = mCursor.getInt(mCursor.getColumnIndex("Quantity"));
            entry = entry - quantity;

            if (entry >= 0) {*/
                ContentValues newValues = new ContentValues();
                newValues.put("salesman_id", salesman_id);
                newValues.put("bussines_id", bussines_id);
                newValues.put("product_name", product_name);
                newValues.put("product_price", product_price);
                newValues.put("quantity", quantity);
                newValues.put("discount", discount);
                newValues.put("total", total);
                newValues.put("type", type);
                newValues.put("Date_added", Date_added);

                db.insert("Account", null, newValues);
               /* ContentValues Values = new ContentValues();
                Values.put("Quantity", entry);
                db.update(PRODUCTS_TABLE, Values, "product_name=?", new String[]{product_name});
                return 1;

            }


        }*/

        return 1;
    }


    public Cursor showdata() {


        db = dbHelper.getReadableDatabase();
        Cursor cursor;
        String[] formdb  = new String[]{"Productname","Quantity","Total","Date","Soldto"};
        cursor = db.query("Account", formdb, null, null, null, null, null);
        return cursor;
    }

    public Cursor getData(String tablename) {


        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + tablename;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }
    public Integer getqty( String product_name)
    {
        int entry = 0;
        Cursor mCursor = db.rawQuery("SELECT Quantity FROM " + "Products" + " WHERE  Productname=?", new String[]{product_name});
        if (mCursor.getCount() > 0) {
            mCursor.moveToFirst();

            entry = mCursor.getInt(mCursor.getColumnIndex("Quantity"));

        }

        return  entry;
    }


}



