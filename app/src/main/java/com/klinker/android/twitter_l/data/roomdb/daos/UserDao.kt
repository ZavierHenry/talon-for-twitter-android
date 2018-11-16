package com.klinker.android.twitter_l.data.roomdb.daos


import androidx.room.*
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.entities.User

@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    internal abstract fun insertUserSpecificInfo(user: User)

    @Update
    abstract fun updateUser(user: User)

    @Delete
    internal abstract fun deleteUser(user: User)

    @Query("DELETE from users WHERE id = :id")
    internal abstract fun deleteUser(id: Long)


    @Query("SELECT id FROM users WHERE screen_name = :screenName")
    abstract fun findUserIdByScreenName(screenName: String): Long


    @Query("SELECT * FROM users WHERE screen_name = :screenName")
    abstract fun findUserByScreenName(screenName: String) : User?

    @Transaction
    open fun insertUser(user: User) {
        val savedUser = findUserByScreenName(user.screenName)

        when {
            savedUser == null -> insertUserSpecificInfo(user)
            savedUser.id!! < 0 || savedUser.id != user.id -> {

                //replace ids in direct messages
                //replace ids in tweets
                //replace ids in favoriteUser
                //??Deal with delete/insert dependency

            }
            savedUser != user -> updateUser(user)

        }

    }








}
