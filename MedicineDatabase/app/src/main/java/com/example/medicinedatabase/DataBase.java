package com.example.medicinedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
    public DataBase( Context context) {
        super(context,"MedicineDB",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table MedicineTable(medicineName TEXT,quantity TEXT,startdate DATE,enddate DATE,time TEXT)");  //it will create database table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public  boolean insertvalues(String medname,String medquantity,String medstartdate,String medenddate,String medtime){
        SQLiteDatabase databse = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("medicineName",medname);
        contentValues.put("quantity",medquantity);
        contentValues.put("startdate",medstartdate);
        contentValues.put("enddate",medenddate);
        contentValues.put("time",medtime);
        long res = databse.insert("MedicineTable", null,contentValues);
        if(res==-1)
            return false;
        else
            return true;
    }

    public Cursor getdata(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor view = database.rawQuery("Select * from MedicineTable",null);
        return view;
    }
}