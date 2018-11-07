package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    internal abstract fun insertUserSpecificInfo(user: User)


    @Delete
    internal abstract fun deleteUser(user: User)

    @Query("DELETE from users WHERE id = :id")
    internal abstract fun deleteUser(id: Long)


    @Query("SELECT id FROM users WHERE screen_name = :screenName")
    internal abstract fun findUserByScreenName(screenName: String): Long


}
