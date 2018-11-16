package com.klinker.android.twitter_l.data.roomdb.daos

import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteUserNotification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
abstract class FavoriteUserNotificationDao {

    @Insert
    abstract fun insertFavoriteUserNotification(notification: FavoriteUserNotification)

    @Query("SELECT * FROM favorite_user_notifications WHERE id = :tweetId")
    abstract fun getFavoriteUserNotification(tweetId: Long): FavoriteUserNotification


    @Query("DELETE FROM favorite_user_notifications WHERE id < (SELECT id FROM favorite_user_notifications ORDER BY id DESC LIMIT 1 OFFSET :trimSize)")
    abstract fun trimDatabase(trimSize: Int)


}
