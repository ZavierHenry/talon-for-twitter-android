package com.klinker.android.twitter_l.data.roomdb.daos

import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteUserNotification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FavoriteUserNotificationDao {

    @Insert
    fun insertFavoriteUserNotification(notification: FavoriteUserNotification)

    @Query("SELECT * FROM favorite_user_notifications WHERE id = :tweetId")
    fun getFavoriteUserNotification(tweetId: Long): FavoriteUserNotification

}
