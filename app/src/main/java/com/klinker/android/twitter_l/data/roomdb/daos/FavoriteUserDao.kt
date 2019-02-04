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



    @Query("SELECT screen_name FROM favorite_users JOIN users ON favorite_users.user_id = users.id AND account = :account")
    internal abstract fun getNames(account: Int) : List<String>

    @Query("SELECT favorite_users.id FROM favorite_users JOIN users ON favorite_users.user_id = users.id AND account = :account AND screen_name = :screenName")
    internal abstract fun findIdByScreenName(screenName: String, account: Int) : Long?


    fun isFavoriteUser(screenName: String, account: Int) : Boolean {
        return findIdByScreenName(screenName, account) != null
    }



}
