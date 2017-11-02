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

public class TableControllerUser extends DatabaseHandler {

    public TableControllerUser(Context context) {
        super(context);
    }

    public boolean create(UserObject userObject) {

        ContentValues values = new ContentValues();

        values.put("firstname", userObject.firstname);
        values.put("lastname", userObject.lastname);
        values.put("email", userObject.email);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("users_table", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public int count(String email) {

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM users_table WHERE email ='" + email + "'";
        Log.v("Query : ", sql);
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();
        return recordCount;
    }

    public List<UserObject> read() {

        List<UserObject> recordsList = new ArrayList<UserObject>();

        String sql = "SELECT * FROM users_table ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String studentFirstname = cursor.getString(cursor.getColumnIndex("firstname"));
                String studentEmail = cursor.getString(cursor.getColumnIndex("email"));

                UserObject UserObject = new UserObject();
                UserObject.id = id;
                UserObject.firstname = studentFirstname;
                UserObject.email = studentEmail;

                recordsList.add(UserObject);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public UserObject readSingleRecord(String userEmail) {

        UserObject userObject = null;

        //String sql = "SELECT * FROM Users  WHERE id = " + studentId;
        String sql = "SELECT * FROM users_table WHERE email ='" + userEmail + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String firstname = cursor.getString(cursor.getColumnIndex("firstname"));
            String lastname = cursor.getString(cursor.getColumnIndex("lastname"));
            String email = cursor.getString(cursor.getColumnIndex("email"));

            userObject = new UserObject();
            userObject.id = id;
            userObject.firstname = firstname;
            userObject.lastname = lastname;
            userObject.email = email;

        }

        cursor.close();
        db.close();

        return userObject;

    }


    public boolean update(UserObject userObject) {

        ContentValues values = new ContentValues();

        values.put("firstname", userObject.firstname);
        values.put("lastname", userObject.lastname);
        values.put("email", userObject.email);

        String where = "email = ?";

        // String[] whereArgs = { Integer.toString(UserObject.id) };
        String[] whereArgs = {userObject.email};

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("users_table", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;

    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("students", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }
}
