package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.ListTweet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class ListTweetDao {

    //insert list tweet
    @Insert
    abstract fun insertListTweet(listTweet: ListTweet)

    //insert list tweets
    @Insert
    abstract fun insertListTweets(listTweets: List<ListTweet>)

    @Delete
    abstract fun deleteListTweet(listTweet: ListTweet)

    @Query("DELETE FROM list_tweets WHERE tweet_id = :tweetId")
    abstract fun deleteListTweet(tweetId: Long)

    @Query("DELETE FROM list_tweets WHERE list_id = :listId")
    abstract fun deleteAllTweets(listId: Long)

    //getListCursor equivalent

    //getWholeCursor equivalent

    //getLastIds equivalent


    @Query("DELETE FROM list_tweets WHERE list_id = :listId AND id < (SELECT MIN(id) FROM list_tweets WHERE list_id = :listId ORDER BY id DESC LIMIT :trimSize)")
    abstract fun trimDatabase(listId: Long, trimSize: Int)


}
