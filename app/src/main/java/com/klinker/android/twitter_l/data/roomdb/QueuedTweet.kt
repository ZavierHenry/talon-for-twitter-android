package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "queued_tweets")
data class QueuedTweet(
        val text: String,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
)

@Dao
abstract class QueuedTweetDao {

    @Insert
    abstract fun insertQueuedTweet(tweet: QueuedTweet) : Long?

    @Update
    abstract fun updateQueuedTweet(tweet: QueuedTweet)

    @Delete
    abstract fun deleteQueuedTweet(tweet: QueuedTweet)

    @Query("SELECT * FROM queued_tweets WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getQueuedTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<QueuedTweet>

}