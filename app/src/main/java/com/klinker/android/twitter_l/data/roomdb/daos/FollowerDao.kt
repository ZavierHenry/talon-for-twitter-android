package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.Follower
import com.klinker.android.twitter_l.data.roomdb.entities.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import twitter4j.User as TwitterUser

@Dao
abstract class FollowerDao {

    @Transaction
    open fun insertFollower(follower: Follower) {
        insertFollowerOnlyData(follower)
    }

    @Insert
    internal abstract fun insertFollowerOnlyData(follower: Follower)

    @Delete
    internal abstract fun deleteFollower(follower: Follower)

    @Query("DELETE FROM followers WHERE user_id = :userId AND account = :account")
    internal abstract fun deleteFollower(userId: Long, account: Int)

    @Query("DELETE FROM followers WHERE account = :account")
    internal abstract fun deleteAllFollowers(account: Int)

    @Query("UPDATE followers SET user_id = :newId WHERE user_id = :oldId")
    internal abstract fun changeFollowerUserId(oldId: Long, newId: Long)


    //update to return ExtendedUser
    @Transaction
    open fun insertFollower(twitterUser: TwitterUser, account: Int) {
        val follower = Follower(twitterUser, account)
        val user = User(twitterUser)


    }


}