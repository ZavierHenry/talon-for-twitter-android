package com.klinker.android.twitter_l.data.roomdb.daos;


import com.klinker.android.twitter_l.data.roomdb.entities.ListTweet;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ListTweetDao {

    //insert list tweet
    @Insert
    void insertListTweet(ListTweet listTweet);

    //insert list tweets
    @Insert
    void insertListTweets(List<ListTweet> listTweets);

    @Delete
    void deleteListTweet(ListTweet listTweet);

    @Query("DELETE FROM list_tweets WHERE tweet_id = :tweetId")
    void deleteListTweet(long tweetId);

    @Query("DELETE FROM list_tweets WHERE list_id = :listId")
    void deleteAllTweets(long listId);

    //getListCursor equivalent

    //getWholeCursor equivalent

    //getLastIds equivalent


    @Query("DELETE FROM list_tweets WHERE list_id = :listId AND id < (SELECT MIN(id) FROM list_tweets WHERE list_id = :listId ORDER BY id DESC LIMIT :trimSize)")
    void trimDatabase(long listId, int trimSize);



}
