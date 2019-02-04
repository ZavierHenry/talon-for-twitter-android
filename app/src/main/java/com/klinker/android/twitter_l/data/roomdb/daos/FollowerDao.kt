package com.klinker.android.twitter_l.data.roomdb.daos


import android.content.Context
import com.klinker.android.twitter_l.data.roomdb.entities.Follower

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayFollower

import twitter4j.User as TwitterUser

@Dao
abstract class FollowerDao {


    private fun insertFollower(follower: Follower) : Follower? {
        return insertFollowerSpecificInfo(follower)?.let { followerId -> follower.copy(id = followerId) }
    }

    @Query("SELECT * FROM followers WHERE id = :id")
    abstract fun findFollowerById(id: Long) : Follower?

    @Query("SELECT followers.id as id, users.id as user_id, name as user_name, screen_name as user_screen_name, profile_pic as user_profile_pic, is_verified as user_is_verified " +
            "FROM followers JOIN users ON followers.user_id = users.id AND account = :account AND followers.user_id = :userId")
    abstract fun findFollowerByUserId(userId: Long, account: Int) : DisplayFollower?


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


    @Transaction
    open fun deleteFollower(context: Context, follower: DisplayFollower, account: Int) {

        if (follower.user.id != null) {
            deleteFollower(Follower(follower.followerId, account, follower.user.id))
            TalonDatabase.getInstance(context).userDao().deleteUserById(follower.user.id)
        }
    }


    @Transaction
    open fun saveFollower(context: Context, follower: DisplayFollower, account: Int) : DisplayFollower? {
        val user = TalonDatabase.getInstance(context).userDao().saveUser(follower.user) ?: return null
        val followerEntity = Follower(null, account, user.id!!)
        return findFollowerByUserId(user.id, account) ?: insertFollowerSpecificInfo(followerEntity).let { if (it != -1L) follower.copy(followerId = it, user = user) else null }
    }




}