package com.example.ldevarak.socialapp.database;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by ldevarak on 10/22/2017.
 */

public class UserDetails {

    @DatabaseField(columnName = "user_id")
    public String userId;

    @DatabaseField(columnName = "first_name")
    public String firstName;

    @DatabaseField(columnName = "last_name")
    public String lastName;

    @DatabaseField(columnName = "emaIL")
    public String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
