package com.klinker.android.twitter_l.data.roomdb.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_user_notifications")
public class FavoriteUserNotification {

    @PrimaryKey
    public long id;

    public FavoriteUserNotification(long id) {
        this.id = id;
    }

}
