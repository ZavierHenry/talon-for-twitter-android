package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "user_tweets", indices = [Index(value = ["tweet_id", "user_user_id"], unique = true)])
data class UserTweet @JvmOverloads constructor(
        @Embedded val tweet: Tweet,
        @ColumnInfo(name = "account_user_id") val userId: Long,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) : BaseDao.TalonEntity<UserTweet> {
    constructor(status: Status, userId: Long) : this(Tweet(status), userId)

    override fun copyWithId(id: Long): UserTweet {
        return this.copy(id = id)
    }
}

@Dao
abstract class UserTweetDao : BaseDao<UserTweet>() {

    @Query("SELECT * FROM user_tweets WHERE account_user_id = :userId ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getUserTweets(userId: Long, page: Int = 1, pageSize: Int = 250) : List<UserTweet>

    @Query("DELETE FROM user_tweets WHERE account_user_id = :userId and id NOT IN (SELECT id FROM user_tweets WHERE account_user_id = :userId ORDER BY id DESC LIMIT :size)")
    abstract fun trimDatabase(userId: Long, size: Int)

    @Ignore
    fun create(status: Status, userId: Long) : UserTweet {
        return this.insert(UserTweet(status, userId))
    }

}