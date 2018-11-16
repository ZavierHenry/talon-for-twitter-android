package com.klinker.android.twitter_l.data.roomdb.daos


import android.content.Context
import android.database.Cursor

import com.klinker.android.twitter_l.data.roomdb.entities.SavedTweet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet
import twitter4j.Status

@Dao
abstract class SavedTweetDao : TweetDao() {

    @Insert
    internal abstract fun insertSavedTweet(savedTweet: SavedTweet)


    @Insert
    internal abstract fun insertSavedTweets(savedTweets: List<SavedTweet>)

    @Delete
    internal abstract fun deleteSavedTweet(savedTweet: SavedTweet)

    @Query("DELETE FROM saved_tweets WHERE tweet_id = :tweetId AND account = :account")
    internal abstract fun deleteSavedTweet(tweetId: Long, account: Int)

    @Query("DELETE FROM saved_tweets WHERE account = :account")
    internal abstract fun deleteSavedTweets(account: Int)

    //getCursor equivalent
    @Query("SELECT * FROM saved_tweets WHERE account = :account ORDER BY tweet_id ASC")
    internal abstract fun getSavedTweetsCursor(account: Int): Cursor

    //isSavedTweet equivalent
    @Query("SELECT id FROM saved_tweets WHERE tweet_id = :tweetId AND account = :account LIMIT 1")
    internal abstract fun isTweetSaved(tweetId: Long, account: Int): Boolean


    @Transaction
    open fun insertSavedTweet(context: Context, status: Status, account: Int) {
        val tweet = Tweet(status)
        val savedTweet = SavedTweet(status, account)

        TalonDatabase.getInstance(context).tweetDao().insertTweet(tweet)
        insertSavedTweet(savedTweet)
    }



}
