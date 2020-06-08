package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "queued_tweets")
data class QueuedTweet(
        val text: String,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
)

@Dao
interface QueuedTweetDao {

    @Insert
    fun insertQueuedTweet(tweet: QueuedTweet) : Long?

    @Delete
    fun deleteQueuedTweet(tweet: QueuedTweet)


    @Query("SELECT * FROM queued_tweets WHERE account = :account")
    fun getQueuedTweets(account: Int) : List<QueuedTweet>

}