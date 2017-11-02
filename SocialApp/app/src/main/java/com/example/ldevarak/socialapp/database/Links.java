package com.example.ldevarak.socialapp.database;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by ldevarak on 10/22/2017.
 */

public class Links {
    @DatabaseField(columnName = "user_id")
    public String userId;

    @DatabaseField(columnName = "name")
    public String name;

    @DatabaseField(columnName = "link")
    public String link;

    @DatabaseField(columnName = "fav")
    public boolean fav;

    @DatabaseField(columnName = "category")
    public String category;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
