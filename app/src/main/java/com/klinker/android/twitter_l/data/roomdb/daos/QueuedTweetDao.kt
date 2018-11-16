package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.QueuedTweet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QueuedTweetDao {

    @Insert
    fun insertQueuedTweet(queuedTweet: QueuedTweet) : Long

    @Delete
    fun deleteQueuedTweet(queuedTweet: QueuedTweet)

    @Query("SELECT * FROM queued_tweets WHERE account = :account")
    fun getQueuedTweets(account: Int): List<QueuedTweet>

    @Query("DELETE FROM queued_tweets")
    fun deleteAllQueuedTweets()


}
