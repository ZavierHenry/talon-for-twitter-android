package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.User as TwitterUser


@Entity(tableName = "followers")
data class Follower(
        @Embedded val user: User,
        val account: Int,
        @PrimaryKey val id: Long? = null
) {
    constructor(user: TwitterUser, account: Int) : this(User(user), account)
}


interface FollowerDao {

    @Insert
    fun insertFollower(follower: Follower) : Long?

    @Delete
    fun deleteFollower(follower: Follower)

    @Query("SELECT * FROM followers WHERE account = :account")
    fun getFollowers(account: Int)

}