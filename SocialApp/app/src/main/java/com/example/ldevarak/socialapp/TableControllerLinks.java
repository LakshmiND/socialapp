package com.example.ldevarak.socialapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldevarak on 10/16/2017.
 */

public class TableControllerLinks extends DatabaseHandler {

    public TableControllerLinks(Context context) {
        super(context);
    }

    public boolean create(LinksObject linksObject) {

        ContentValues values = new ContentValues();

        values.put("linkname", linksObject.linkname);
        values.put("url", linksObject.url);
        values.put("category", linksObject.category);
        values.put("favourite", linksObject.favourite);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("links_table", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public int count(String email) {

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM links_table WHERE email ='" + email + "'";
        Log.v("Query : ", sql);
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();
        return recordCount;
    }

    public List<LinksObject> read() {

        List<LinksObject> recordsList = new ArrayList<LinksObject>();

        String sql = "SELECT * FROM links_table ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String linkname = cursor.getString(cursor.getColumnIndex("linkname"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String favourite = cursor.getString(cursor.getColumnIndex("favourite"));

                LinksObject linksObject = new LinksObject();
                linksObject.id = id;
                linksObject.linkname = linkname;
                linksObject.url = url;
                linksObject.category = category;
                linksObject.favourite = favourite;

                recordsList.add(linksObject);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public List<LinksObject> categoryList(String categry) {

        List<LinksObject> recordsList = new ArrayList<LinksObject>();
        String sql = "SELECT * FROM links_table WHERE category="+"'"+categry+"'";

        //String sql = "SELECT * FROM links_table ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String linkname = cursor.getString(cursor.getColumnIndex("linkname"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String favourite = cursor.getString(cursor.getColumnIndex("favourite"));

                LinksObject linksObject = new LinksObject();
                linksObject.id = id;
                linksObject.linkname = linkname;
                linksObject.url = url;
                linksObject.category = category;
                linksObject.favourite = favourite;

                recordsList.add(linksObject);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }



    public LinksObject readSingleRecord(int linkId) {

        LinksObject linksObject = null;

        String sql = "SELECT * FROM links_table WHERE id = " + linkId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String linkname = cursor.getString(cursor.getColumnIndex("linkname"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String category = cursor.getString(cursor.getColumnIndex("category"));
            String favourite = cursor.getString(cursor.getColumnIndex("favourite"));

            linksObject = new LinksObject();
            linksObject.id = id;
            linksObject.linkname = linkname;
            linksObject.url = url;
            linksObject.category = category;
            linksObject.favourite = favourite;

        }

        cursor.close();
        db.close();

        return linksObject;

    }


    public boolean update(LinksObject linksObject) {
        ContentValues values = new ContentValues();
        values.put("linkname", linksObject.linkname);
        values.put("url", linksObject.url);
        values.put("category", linksObject.category);
        values.put("favourite", linksObject.favourite);

        String where = "id = ?";
        String[] whereArgs = {Integer.toString(linksObject.id)};
        SQLiteDatabase db = this.getWritableDatabase();
        boolean updateSuccessful = db.update("links_table", values, where, whereArgs) > 0;
        db.close();
        return updateSuccessful;

    }

    public boolean delete(int linkId) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("links_table", "id ='" + linkId + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }
}
