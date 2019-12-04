package com.example.androidlabs;


import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MyDatabaseOpenHelperRecipe extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDatabaseFile";
    public static final int VERSION_NUM = 2;
    public static final String TABLE_NAME = "Recipe";
    public static final String COL_ID = "_id";
    public static final String COL_TEXT = "TEXT";



    public MyDatabaseOpenHelperRecipe(Activity ctx) {
        //The factory parameter should be null, unless you know a lot about Database Memory management

//        Context ctx – the Activity where the database is being opened.
//        String databaseName – this is the file that will contain the data.
//        CursorFactory – An object to create Cursor objects, normally this is null.
//        int version – What is the version of your database


        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
//
//    If the database file doesn’t exist yet, then onCreate gets called immediately by Android.
//            onCreate(SQLiteDatabase db) : db is a database object given by Android for running SQL commands.
//    This function is used to execute a table creation statement in SQL:
//            db.execSQL( “CREATE TABLE “ + name + “ ( _id INTEGER 	PRIMARY KEY AUTOINCREMENT, NAME text, EMAIL text);” );

    public void onCreate(SQLiteDatabase db)
    {
        //db.execSQL(  ) is a function that executes a string SQL statement

        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TEXT + " TEXT)");
    }


//    If the database does exist on the device, and the version in the constructor is newer than the version that exists on the device,
//    then onUpgrade gets called.
//    onUpgrade(SQLiteDatabase db, int oldVer, int newVer)

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }




}