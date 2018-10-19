package com.klinker.android.twitter_l.data.roomdb;


import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface QueuedTweetDao {

    @Insert
    void insertQueuedTweet(QueuedTweet queuedTweet);

    @Delete
    void deleteQueuedTweet(QueuedTweet queuedTweet);

    @Query("DELETE FROM queued_tweets WHERE text = :message AND account = :account")
    void deleteQueuedTweet(String message, int account);

    @Query("SELECT * FROM queued_tweets WHERE account = :account")
    List<QueuedTweet> getQueuedTweets(int account);


}
