package com.hamza.inventory.SQLite_DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.hamza.inventory.Date_Models.Products_model;

import java.sql.SQLException;
import java.util.ArrayList;


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
            + "saleString  varchar);); ";

    private static final String CUSTOMERS_TABLE = "customers";
    static final String DATABASE_CUSTOMER = "create table " + "customers" + "( "
            + "CustomerID" + " varchar PRIMARY KEY , "
            + "bussiness_name  varchar, personal_name varchar , address varchar ,distrcit varchar ," +
            "mobile INTEGER ,salesman INTEGER );); ";

    private static final String RECOVRY_TABLE = "recovry";
    static final String DATABASE_RECOVRY = "create table " + "recovry" + "( "
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "businnes_name  INTEGER, amount_paid INTEGER , amount_remaining INTEGER ,total_bill INTEGER ," +
            "salesman_id INTEGER , isupdated INTERGER);); ";




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

    public void insertProduct(String Productname, Integer Quantity ,Integer Trade_Price ,Integer Retail_Price )
            {
                    ContentValues newValues = new ContentValues();
                    newValues.put("Productname", Productname);
                    newValues.put("Quantity", Quantity);
                    newValues.put("Trade_Price", Trade_Price);
                    newValues.put("Retail_Price", Retail_Price);
                   long res =  db.insert(PRODUCTS_TABLE, null, newValues);



             }

    public void insertRecovry(String businnes_name, Integer total_bill ,Integer amount_remaining ,Integer amount_paid ,Integer salesman_id)
    {

        String selectQuery = "SELECT  * FROM " +RECOVRY_TABLE+" WHERE businnes_name = "+businnes_name;

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor != null && cursor.getCount() > 0)
        {
            ContentValues newValues = new ContentValues();
            newValues.put("businnes_name", businnes_name);
            newValues.put("total_bill", total_bill);
            newValues.put("amount_remaining", amount_remaining);
            newValues.put("amount_paid", amount_paid);
            newValues.put("salesman_id", salesman_id);
            newValues.put("isupdated", 0);
            int res = db.update(RECOVRY_TABLE, newValues, "businnes_name" + "="+businnes_name,
                    null);
            Toast.makeText(context, "Updated in local data base", Toast.LENGTH_SHORT).show();


       }
        else
        {

            ContentValues newValues = new ContentValues();
            newValues.put("businnes_name", businnes_name);
            newValues.put("total_bill", total_bill);
            newValues.put("amount_remaining", amount_remaining);
            newValues.put("amount_paid", amount_paid);
            newValues.put("salesman_id", salesman_id);
            newValues.put("isupdated", 0);
            long res =  db.insert(RECOVRY_TABLE, null, newValues);
            Toast.makeText(context, "Enterd in local data base", Toast.LENGTH_SHORT).show();


        }

    }


    public void updateRecovry(int amount_remaining,int amount_paid,String businnes_name)
    {
        ContentValues newValues = new ContentValues();

        newValues.put("amount_remaining", amount_remaining);
        newValues.put("amount_paid", amount_paid);
        newValues.put("isupdated", 1);
        int res = db.update(RECOVRY_TABLE, newValues, "businnes_name" + "="+businnes_name,
                null);
    }

    public void insertBussines(String bussines_name,String id,String personal_name, String address ,String distrcit ,String mobile,String salesman )
    {
        ContentValues newValues = new ContentValues();
        newValues.put("bussiness_name", bussines_name);
        newValues.put("CustomerID", id);
        newValues.put("personal_name", personal_name);
        newValues.put("address", address);
        newValues.put("distrcit", distrcit);
        newValues.put("mobile", mobile);
        newValues.put("salesman", salesman);
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

   /* public ArrayList<Customer_model> getAllCustomers() {
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
    }*/

    public ArrayList<String> getAllSales() {
        ArrayList<String> List = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ACCOUNTS_TABLE;

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String sales = "";
        // looping through all rows and adding to list
        if(cursor.equals(""))
        {


        }
        else
        {
            if (cursor.moveToFirst()) {
                do {

                    List.add(cursor.getString(1));

                } while (cursor.moveToNext());
            }
        }


        // return contact list
        return List;
    }

    public Integer insertSales(String salesString) {

      /*  Cursor mCursor = db.rawQuery("SELECT Quantity FROM " + "Sales" + " WHERE  Productname=?", new String[]{product_name});
        if (mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            int entry;
            entry = mCursor.getInt(mCursor.getColumnIndex("Quantity"));
            entry = entry - quantity;

            if (entry >= 0) {*/
                ContentValues newValues = new ContentValues();
                newValues.put("saleString", salesString);
                long result = db.insert("Sales", null, newValues);


               /* ContentValues Values = new ContentValues();
                Values.put("Quantity", entry);
                db.update(PRODUCTS_TABLE, Values, "product_name=?", new String[]{product_name});
                return 1;

            }


        }*/

        return 1;
    }


    public Cursor showRecovry() {


        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + RECOVRY_TABLE +" WHERE isupdated = 1" ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getData(String tablename) {


        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + tablename;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }


    public void updatestatus(String bussid)
    {
        ContentValues newValues = new ContentValues();


        newValues.put("isupdated", 0);
        int res = db.update(RECOVRY_TABLE, newValues, "businnes_name" + "="+bussid,
                null);
    }

    public Cursor getRecords(String tablename,String bussinesid) {


        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + tablename +" WHERE businnes_name = " + bussinesid;
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



