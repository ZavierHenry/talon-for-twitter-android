package com.klinker.android.twitter_l.data.roomdb;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class UserTweetDao extends TweetDao {


    @Insert
    abstract void insertUserTweet(UserTweet userTweet);

    @Query("DELETE FROM user_tweets WHERE tweet_id = :tweetId")
    abstract void deleteUserTweet(long tweetId);

    @Query("DELETE FROM user_tweets WHERE user_id = :userId")
    abstract void deleteTweetsForUser(long userId);


    //possibly put an index on tweet_id column to improve performance
    @Query("DELETE FROM user_tweets WHERE user_id = :userId AND tweet_id < (SELECT MIN(tweet_id) FROM user_tweets WHERE user_id = :userId ORDER BY tweet_id DESC LIMIT :trimSize)")
    abstract void trimDatabase(long userId, int trimSize);


}
