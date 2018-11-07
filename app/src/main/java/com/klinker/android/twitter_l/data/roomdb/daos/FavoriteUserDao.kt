package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteUser
import com.klinker.android.twitter_l.data.roomdb.entities.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class FavoriteUserDao {


    @Insert
    internal abstract fun insertFavoriteUserSpecificInfo(user: FavoriteUser)


    @Delete
    internal abstract fun deleteUser(user: FavoriteUser)

    @Query("DELETE FROM favorite_users WHERE user_id = :userId AND account = :account")
    internal abstract fun deleteUser(userId: Long, account: Int)

    @Query("DELETE FROM favorite_users WHERE account = :account")
    internal abstract fun deleteAllUsers(account: Int)

    //getCursor implementation
    @Query("SELECT * FROM favorite_users WHERE account = :account")
    internal abstract fun getAllFavoriteUsers(account: Int): List<FavoriteUser>

    @Query("SELECT users.id, name, screen_name, profile_pic, is_verified FROM favorite_users JOIN users ON user_id = users.id AND account = :account AND user_id = :userId")
    internal abstract fun getFavoriteUser(userId: Long, account: Int): User

    @Query("SELECT users.screen_name FROM favorite_users JOIN users ON favorite_users.user_id = users.id AND account = :account")
    internal abstract fun getFavoriteUserScreenNames(account: Int): List<String>

    @Query("SELECT favorite_users.id FROM favorite_users JOIN users ON favorite_users.user_id = users.id AND users.screen_name = :screenName AND favorite_users.account = :account")
    internal abstract fun isFavoriteUser(screenName: String, account: Int): Boolean


    @Query("SELECT id FROM favorite_users WHERE user_id = :userId AND account = :account")
    internal abstract fun isFavoriteUser(userId: Long, account: Int): Boolean


    @Query("UPDATE favorite_users SET user_id = :newUserId WHERE user_id = :oldUserId")
    internal abstract fun changeFavoriteUserId(oldUserId: Long, newUserId: Long)


}
