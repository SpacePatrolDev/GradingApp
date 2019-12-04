package com.example.gradingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHandler {

    //Declare Database name and Table name
    String Database = "CollegeDB";
    String Table = "StudentTB";

    private SQLiteDatabase SQLdb;

    public DatabaseHandler(Context context){
        //Open or Create the Database
        SQLdb = context.openOrCreateDatabase(Database, Context.MODE_PRIVATE, null);
        SQLdb.execSQL("Create Table IF NOT Exists " +
                Table+"(ID Integer Primary Key Autoincrement," +
                "FName Varchar," +
                "LName Varchar," +
                "Marks Varchar," +
                "Course Varchar," +
                "Credit Integer);");
    }

    //Function to get all the records from database
    public Cursor getAllData(){
        return SQLdb.rawQuery("SELECT * FROM "+Table, null);
    }

    //Function to get all the records based on ID
    public Cursor getDataByID (String ID) {
        return SQLdb.rawQuery("SELECT * FROM "+Table+" WHERE ID = "+ID, null);
    }

    //Function to get all the records based on Program
    public Cursor getDataByProg (String Prog) {
        return SQLdb.rawQuery("SELECT * FROM "+Table+" WHERE Course = '"+Prog+"'", null);
    }

    //Function to add record to the database
    public boolean AddData(ContentValues cv)
    {
        try{
            SQLdb.insert(Table, null, cv);
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }

    //Function to update record in the database
    public boolean UpdateData(ContentValues cv, String ID)
    {
        try{
            SQLdb.update(Table, cv, "ID = "+ID, null);
            return true;
        }catch(Exception e)
        {
            return false;
        }
    }

    //Function to delete record in the database
    public boolean DeleteData(String ID)
    {
        try{
            SQLdb.delete(Table, "ID = "+ID, null);
            return true;
        }catch(Exception e)
        {
            return false;
        }
    }
}
