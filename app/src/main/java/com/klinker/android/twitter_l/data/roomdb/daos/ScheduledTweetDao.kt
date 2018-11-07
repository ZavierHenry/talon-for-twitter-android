package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.ScheduledTweet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScheduledTweetDao {

    @Insert
    fun insertScheduledTweet(scheduledTweet: ScheduledTweet)

    @Delete
    fun deleteScheduledTweet(scheduledTweet: ScheduledTweet)

    @Query("SELECT * FROM scheduled_tweets WHERE account = :account")
    fun getScheduledTweets(account: Int): List<ScheduledTweet>

    @Query("SELECT * FROM scheduled_tweets WHERE time > :time LIMIT 1")
    fun getEarliestScheduledTweets(time: Long): ScheduledTweet

}
