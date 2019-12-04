package com.example.androidlabs;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class create database and tables
 *
 *
 * Class to create table in the database
 * public class DatabaseHelper extends SQLiteOpenHelper
 *
 *  //The factory parameter should be null, unless you know a lot about Database Memory management
 * constructor to et database name and version number
 *  public NewsArticleDB(Activity ctx){
 *
 *
 *  Create table and its columns in the database
 *
 *
 *   public void onCreate(SQLiteDatabase db) {
 *
 *   upgrade database if there is a newer version
 *   public void onUpgrade
 */




public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ecsDatabaseFile";

    public static final int VERSION_NUM = 3;

    public static final String TABLE_NAME = "Charging_Stations";

    public static final String COL_ID = "ID";

    public static final String COL_TITLE = "TITLE";

    public static final String COL_LONGITUDE = "Longitude";

    public static final String COL_LATITUDE = "Latitude";

    public static final String COL_PHONENO = "Phone";




    public DatabaseHelper (Activity ctx) {
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, "
                + COL_LATITUDE + " TEXT, "
                + COL_LONGITUDE + " TEXT, "
                + COL_PHONENO + " TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);

        //Deletes old table:
/**
 * Remove/delete old exisitng table
 */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Creates new table:

        onCreate(db);
    }
}
