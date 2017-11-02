package com.example.ldevarak.socialapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by ldevarak on 10/22/2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "talentOutReach.db";
    private static final int DATABASE_VERSION = 2;

    private Dao<Links, Integer> linksDao;
    private Dao<UserDetails, Integer> userDetailsesDao;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Links.class);
            TableUtils.createTable(connectionSource, UserDetails.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, Links.class, true);
            TableUtils.dropTable(connectionSource, UserDetails.class, true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    public Dao<Links, Integer> getLinkDao() throws SQLException {
        if (linksDao == null) {
            linksDao = getDao(Links.class);
        }
        return linksDao;
    }

//
//    public void updateLinkDao() throws SQLException {
//
//        linksDao = getDao(Links.class);
//        UpdateBuilder<Links, Integer> updateBuilder = linksDao.updateBuilder();
//        updateBuilder.where().eq("fav", value);
//
//        Set<Map.Entry<String, Object>> valueSet = values.valueSet();
//        Iterator<Entry<String, Object>> it = valueSet.iterator();
//        while(it.hasNext()){
//            Entry<String, Object> valueEntry = it.next();
//            updateBuilder.updateColumnValue(valueEntry.getKey(), valueEntry.getValue());
//        }
//
////			for (String key : values.keySet()) {
////				updateBuilder.updateColumnValue(key, values.get(key));
////			}
//        return updateBuilder.update();
//
//    }





    public Dao<UserDetails, Integer> getUserDetailsesDao() throws SQLException {
        if (userDetailsesDao == null) {
            userDetailsesDao = getDao(UserDetails.class);
        }
        return userDetailsesDao;
    }
    @Override

    public void close() {

        super.close();

        linksDao = null;

        userDetailsesDao = null;

    }
}