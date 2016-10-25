package com.hamza.inventory.SQLite_DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;


public class Database {

    static final String DATABASE_NAME = "Neelam_Local.db";
    static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "Products";
    static final String DATABASE_CREATE = "create table " + "Products" + "( "
            + "ProductID" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "Productname  varchar, Quantity INTEGER , Trade_Price INTEGER , Retail_Price INTEGER);); ";

    static final String DATABASE_ACCOUNTS = "create table " + "Sales" + "( "
            + "ProductID" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "productName  varchar, salesman_id INTEGER , bussines_id INTEGER ,product_price INTEGER ," +
            "quantity INTEGER ,discount INTEGER , total INTEGER , amount_paid INTEGER , amount_remaining INTEGER);); ";

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

    public void insertProduct(String Productname, Integer qauntity)
    {

        Cursor mCursor = db.rawQuery("SELECT Quantity FROM " + DATABASE_TABLE + " WHERE  Productname=?", new String[]{Productname});

        if(mCursor.getCount() >0)
        {
            mCursor.moveToFirst();
            int entry;
            entry = mCursor.getInt(mCursor.getColumnIndex("Quantity"));
            entry = entry+qauntity;

            ContentValues newValues = new ContentValues();
            newValues.put("Quantity", entry);
            db.update(DATABASE_TABLE, newValues, "Productname=?", new String[]{Productname});



        }

        else if((mCursor.getCount() == 0))
        {


            ContentValues newValues = new ContentValues();
            newValues.put("Productname", Productname);
            newValues.put("Quantity", qauntity);
            db.insert(DATABASE_TABLE, null, newValues);

        }



    }

    public Integer insertSales(String Productname, Integer qauntity, Integer total, String date,String soldto)
    {
        Cursor mCursor = db.rawQuery("SELECT Quantity FROM " + "Products" + " WHERE  Productname=?", new String[]{Productname});
        if(mCursor.getCount() >0) {
            mCursor.moveToFirst();
            int entry;
            entry = mCursor.getInt(mCursor.getColumnIndex("Quantity"));
            entry = entry-qauntity;

            if(entry>0) {
                ContentValues newValues = new ContentValues();
                newValues.put("Productname", Productname);
                newValues.put("Quantity", qauntity);
                newValues.put("Total", total);
                newValues.put("Date", date);
                newValues.put("Soldto",soldto);
                db.insert("Account", null, newValues);
                ContentValues Values = new ContentValues();
                Values.put("Quantity", entry);
                db.update(DATABASE_TABLE, Values, "Productname=?", new String[]{Productname});
                return 1;
            }

        }

        return 0;


    }

    public Cursor showdata() {


        db = dbHelper.getReadableDatabase();
        Cursor cursor;
        String[] formdb  = new String[]{"Productname","Quantity","Total","Date","Soldto"};
        cursor = db.query("Account",formdb,null,null,null,null,null);
        return cursor;
    }

    public Cursor products() {


        db = dbHelper.getReadableDatabase();
        Cursor cursor;
        String[] formdb  = new String[]{"Productname","Quantity"};
        cursor = db.query(DATABASE_TABLE,formdb,null,null,null,null,null);
        return cursor;
    }
    public  Cursor getqty()
    {
        db = dbHelper.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT Quantity FROM Products",null,null);
        return mCursor;

    }


}



