package com.example.ldevarak.socialapp.database;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ldevarak on 10/22/2017.
 */

public class DatabaseUtils {


    private DatabaseHelper helper;
    public DatabaseUtils(Context ctx) {

        helper = new DatabaseHelper(ctx);
    }

    public DatabaseHelper getHelper() {

        return helper;
    }



    public void addUserDetails(UserDetails userDetails) {
        try {
            final Dao<UserDetails, Integer> userDetailsesDao = getHelper().getUserDetailsesDao();
            userDetailsesDao.create(userDetails);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public List<UserDetails> getUserDetails() {
        List<UserDetails> userDetails=null;
        try {
            final Dao<UserDetails, Integer> userDetailsesDao =getHelper().getUserDetailsesDao();
            userDetails =  userDetailsesDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userDetails;

    }


    public void addLinks(Links links) {
        try {
            final Dao<Links, Integer> linkDao= getHelper().getLinkDao();
            linkDao.create(links);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public List<Links> getLinks() {
        List<Links> links=null;
        try {
            final Dao<Links, Integer> linkDao =getHelper().getLinkDao();
            links =  linkDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return links;

    }

    public void deleteUserDetails() {
        try {
            final Dao<UserDetails, Integer> userDetailsesDao =getHelper().getUserDetailsesDao();
            DeleteBuilder<UserDetails, Integer> deleteBuilder = userDetailsesDao.deleteBuilder();
            deleteBuilder.delete();

        }catch (SQLException e){

        }
    }
    public void deleteLinks() {
        try {
            final Dao<Links, Integer> linkDao =getHelper().getLinkDao();
            DeleteBuilder<Links, Integer> deleteBuilder = linkDao.deleteBuilder();
            deleteBuilder.delete();

        }catch (SQLException e){

        }
    }
}
