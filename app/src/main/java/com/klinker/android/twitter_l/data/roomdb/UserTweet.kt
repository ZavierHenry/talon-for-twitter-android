package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "user_tweets")
data class UserTweet(
        @Embedded val tweet: Tweet,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    constructor(status: Status, account: Int) : this(Tweet(status), account)
}


@Dao
interface UserTweetDao {

    @Insert
    fun insertUserTweet(userTweet: UserTweet) : Long?

    @Delete
    fun deleteUserTweet(userTweet: UserTweet)

    @Query("SELECT * FROM user_tweets WHERE account = :account ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    fun getUserTweets(account: Int, page: Int = 1, pageSize: Int = 250) : List<UserTweet>

}