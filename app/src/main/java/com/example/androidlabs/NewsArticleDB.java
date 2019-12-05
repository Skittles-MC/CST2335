package com.example.androidlabs;

import android.database.sqlite.SQLiteOpenHelper;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**********************************************************************
 Filename: NewsArticleDB.java
 Version: 1.5
 Authors:	Martin Choy
 Student No:	040835431
 Course Name/Number:	CST2335 Mobile Graphical Interface Programming
 Lab Sect:	013
 Assignment #: Final Project - 1
 Assignment name:  Final_GroupProject F19
 Due Date: Dec 4th 2019 , 11:59PM midnight
 Submission Date: Dec 4th 2019
 Professor: Shahzeb Khowaja
 *********************************************************************/

/**
 * Main purpose of this class is used to create the database and tables needed.
 */
public class NewsArticleDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "NewsDatabase";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "NewsHistory";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "NAME";
    public static final String COL_AUTHOR = "AUTHOR";
    public static final String COL_TITLE = "TITLE";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_URL = "URL";
    public static final String COL_URL_TO_IMAGE = "URL_TO_IMAGE";
    public static final String COL_PUBLISHED_AT = "PUBLISHED_AT";
    public static final String COL_CONTENT = "CONTENT";


    /**
     * Method used unify database name and it's version number...
     * @param newsDB
     */
    public NewsArticleDB(Activity newsDB){
        super(newsDB, DATABASE_NAME, null, VERSION_NUM );
    }

    /**
     * Method used create the table and columns for the database
     * @param newsDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase newsDatabase) {
        newsDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + " TEXT, " + COL_AUTHOR + " TEXT,"
                + COL_TITLE + " TEXT, "
                + COL_DESCRIPTION + " TEXT, "
                + COL_URL + " TEXT, "
                + COL_URL_TO_IMAGE + " TEXT, "
                + COL_PUBLISHED_AT + " TEXT, "
                + COL_CONTENT + " TEXT " + ")");
    }

    /**
     * Methods used to upgrade database if the current one is out of date
     * @param newsDB
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase newsDB, int oldVersion, int newVersion) {
        Log.i("Database UPGRADE", "Old version Number:" + oldVersion + " New Database Version:"+newVersion);
        newsDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(newsDB);
    }
}
