package com.example.ldevarak.socialapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ldevarak on 10/16/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "SocialAppDatabase";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String sql = "CREATE TABLE users_table " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "firstname TEXT, " +
                "lastname TEXT, " +
                "email TEXT ) ";
        db.execSQL(sql);

        String linksQuery = "CREATE TABLE links_table " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "linkname TEXT, " +
                "url TEXT, " +
                "category TEXT, " +
                "favourite TEXT ) ";

        db.execSQL(linksQuery);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        String sql = "DROP TABLE IF EXISTS users_table";
        db.execSQL(sql);

        String linksQuery = "DROP TABLE IF EXISTS links_table";
        db.execSQL(linksQuery);

        onCreate(db);
    }

}