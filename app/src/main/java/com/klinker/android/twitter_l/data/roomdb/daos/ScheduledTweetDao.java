package com.klinker.android.twitter_l.data.roomdb.daos;


import com.klinker.android.twitter_l.data.roomdb.entities.ScheduledTweet;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ScheduledTweetDao {

    @Insert
    void insertScheduledTweet(ScheduledTweet scheduledTweet);

    @Delete
    void deleteScheduledTweet(ScheduledTweet scheduledTweet);

    @Query("SELECT * FROM scheduled_tweets WHERE account = :account")
    List<ScheduledTweet> getScheduledTweets(int account);

    @Query("SELECT * FROM scheduled_tweets WHERE time > :time LIMIT 1")
    ScheduledTweet getEarliestScheduledTweets(long time);

}
