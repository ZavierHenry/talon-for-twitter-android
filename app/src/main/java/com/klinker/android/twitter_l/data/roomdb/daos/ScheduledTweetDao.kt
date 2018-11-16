package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.ScheduledTweet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScheduledTweetDao {

    @Insert
    fun insertScheduledTweet(scheduledTweet: ScheduledTweet) : Long

    @Delete
    fun deleteScheduledTweet(scheduledTweet: ScheduledTweet)

    @Query("SELECT alarm_id, text, account, time FROM scheduled_tweets WHERE account = :account")
    fun getScheduledTweets(account: Int): List<ScheduledTweet>

    @Query("SELECT time FROM scheduled_tweets WHERE time > :time LIMIT 1")
    fun getEarliesScheduledTweetTime(time: Long) : Long

    @Query("SELECT alarm_id, text, account, time FROM scheduled_tweets WHERE time > :time LIMIT 1")
    fun getEarliestScheduledTweet(time: Long): ScheduledTweet?

}
