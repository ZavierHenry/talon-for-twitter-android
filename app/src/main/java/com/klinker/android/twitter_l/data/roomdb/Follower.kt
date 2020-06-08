package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.User as TwitterUser


@Entity(tableName = "followers")
data class Follower(
        @Embedded val user: User,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    constructor(user: TwitterUser, account: Int) : this(User(user), account)
}

@Dao
interface FollowerDao {

    @Insert
    fun insertFollower(follower: Follower) : Long?

    @Delete
    fun deleteFollower(follower: Follower)

    @Query("SELECT * FROM followers WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    fun getFollowers(account: Int, page: Int = 1, pageSize: Int = 200) : List<Follower>

}