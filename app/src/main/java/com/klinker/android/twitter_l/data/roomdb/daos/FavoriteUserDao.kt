package com.klinker.android.twitter_l.data.roomdb.daos


import android.content.Context
import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteUser

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayFavoriteUser

import twitter4j.User as TwitterUser

@Dao
abstract class FavoriteUserDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertFavoriteUser(user: FavoriteUser)


    fun insertFavoriteUser(context: Context, user: DisplayFavoriteUser) : DisplayFavoriteUser? {
        return null
    }

    @Query("DELETE FROM favorite_users WHERE id = :id")
    abstract fun deleteFavoriteUser(id: Long)


}
