package com.klinker.android.twitter_l.data.roomdb;


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

    @Query("DELETE FROM scheduled_tweets WHERE alarm_id = :alarmId")
    void deleteScheduledTweet(int alarmId);


    @Query("SELECT * FROM scheduled_tweets WHERE account = :account")
    List<ScheduledTweet> getScheduledTweets(int account);

    @Query("SELECT * FROM scheduled_tweets")
    List<ScheduledTweet> getScheduledTweets();

}
