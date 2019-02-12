package com.klinker.android.twitter_l.data.roomdb.daos


import android.content.Context
import com.klinker.android.twitter_l.data.roomdb.entities.Follower

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.entities.User
import com.klinker.android.twitter_l.data.roomdb.entities.UserInteraction
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayFollower

import twitter4j.User as TwitterUser

@Dao
abstract class FollowerDao {


    private fun insertFollower(follower: Follower) : Follower? {
        return insertFollowerSpecificInfo(follower)?.let { followerId -> follower.copy(id = followerId) }
    }

    @Query("SELECT * FROM followers WHERE id = :id")
    abstract fun findFollowerById(id: Long) : Follower?

    @Query("SELECT * FROM followers WHERE account = :account AND followers.user_id = :userId")
    abstract fun findFollowerByUserId(userId: Long, account: Int) : List<Follower>


    @Insert
    internal abstract fun insertFollowerSpecificInfo(follower: Follower) : Long?

    @Delete
    internal abstract fun deleteFollower(follower: Follower)

    @Query("DELETE FROM followers WHERE id = :id")
    internal abstract fun deleteFollowerById(id: Long)


    @Query("SELECT DISTINCT user_id FROM followers WHERE account = :account")
    internal abstract fun getUserIds(account: Int) : List<Long>


    @Query("DELETE FROM followers WHERE account = :account")
    abstract fun deleteAllFollowers(account: Int)


}