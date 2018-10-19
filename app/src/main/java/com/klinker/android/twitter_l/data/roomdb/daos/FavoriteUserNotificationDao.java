package com.klinker.android.twitter_l.data.roomdb.daos;

import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteUserNotification;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface FavoriteUserNotificationDao {

    @Insert
    void insertFavoriteUserNotification(FavoriteUserNotification notification);

    @Query("SELECT id FROM favorite_user_notifications WHERE id = :tweetId")
    FavoriteUserNotification getFavoriteUserNotification(long tweetId);

}
