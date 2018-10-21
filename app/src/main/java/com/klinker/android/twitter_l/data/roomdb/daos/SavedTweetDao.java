package com.klinker.android.twitter_l.data.roomdb.daos;


import android.database.Cursor;

import com.klinker.android.twitter_l.data.roomdb.entities.SavedTweet;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public abstract class SavedTweetDao extends TweetDao {

    @Insert
    abstract void insertSavedTweet(SavedTweet savedTweet);


    @Insert
    abstract void insertSavedTweets(List<SavedTweet> savedTweets);

    @Delete
    abstract void deleteSavedTweet(SavedTweet savedTweet);

    @Query("DELETE FROM saved_tweets WHERE tweet_id = :tweetId AND account = :account")
    abstract void deleteSavedTweet(long tweetId, int account);

    @Query("DELETE FROM saved_tweets WHERE account = :account")
    abstract void deleteSavedTweets(int account);

    //getCursor equivalent
    @Query("SELECT * FROM saved_tweets WHERE account = :account ORDER BY tweet_id ASC")
    abstract Cursor getSavedTweetsCursor(int account);

    //isSavedTweet equivalent
    @Query("SELECT id FROM saved_tweets WHERE tweet_id = :tweetId AND account = :account LIMIT 1")
    abstract boolean isTweetSaved(long tweetId, int account);


}
