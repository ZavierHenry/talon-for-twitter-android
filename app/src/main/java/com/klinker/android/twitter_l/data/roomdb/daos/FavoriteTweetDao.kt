package com.klinker.android.twitter_l.data.roomdb.daos


import android.content.Context
import android.database.Cursor
import androidx.room.*
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteTweet
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayFavoriteTweet

import twitter4j.Status

@Dao
abstract class FavoriteTweetDao {

    @Insert
    internal abstract fun insertFavoriteTweet(favoriteTweet: FavoriteTweet) : Long?

    @Delete
    internal abstract fun deleteFavoriteTweet(favoriteTweet: FavoriteTweet)

    @Query("DELETE FROM favorite_tweets WHERE tweet_id = :tweetId AND account = :account")
    abstract fun deleteFavoriteTweet(tweetId: Long, account: Int)

    fun deleteFavoriteTweet(displayFavoriteTweet: DisplayFavoriteTweet, account: Int) {
        deleteFavoriteTweet(displayFavoriteTweet.toFavoriteTweet(account))
    }

    @Query("DELETE FROM favorite_tweets WHERE account = :account")
    abstract fun deleteAllFavoriteTweets(account: Int)

    //TODO: get DisplayFavoriteTweets with a limit

    @Query("SELECT tweet_id FROM favorite_tweets WHERE account = :account ORDER BY tweet_id DESC LIMIT :size")
    abstract fun getLatestTweetIds(account: Int, size: Int) : List<Long>


    @Query("DELETE FROM favorite_tweets WHERE account = :account AND tweet_id <= (SELECT tweet_id FROM favorite_tweets WHERE account = :account ORDER BY tweet_id DESC LIMIT 1 OFFSET :trimSize)")
    abstract fun trimDatabase(account: Int, trimSize: Int)

}
