package com.klinker.android.twitter_l.data.roomdb.daos


import android.database.Cursor

import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteTweet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class FavoriteTweetDao {

    @Insert
    internal abstract fun insertFavoriteTweet(favoriteTweet: FavoriteTweet)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    internal abstract fun insertFavoriteTweets(favoriteTweets: List<FavoriteTweet>)


    @Delete
    internal abstract fun deleteFavoriteTweet(favoriteTweet: FavoriteTweet)

    @Query("DELETE FROM favorite_tweets WHERE account = :account AND tweet_id = :tweetId")
    internal abstract fun deleteFavoriteTweet(tweetId: Long, account: Int)

    @Query("DELETE FROM favorite_tweets WHERE account = :account")
    internal abstract fun deleteAllFavoriteTweets(account: Int)


    @Query("SELECT * FROM favorite_tweets JOIN tweets ON tweet_id = tweets.id AND account = :account JOIN users ON user_id = users.id ORDER BY tweet_id DESC LIMIT :timelineSize")
    internal abstract fun getFavoriteTweetsCursor(account: Int, timelineSize: Int): Cursor

    //list of tweets with user info


    @Query("SELECT tweet_id FROM favorite_tweets WHERE account = :account ORDER BY tweet_id DESC LIMIT 5")
    internal abstract fun getLatestFavoriteTweetIds(account: Int): LongArray

    @Query("SELECT id FROM favorite_tweets WHERE tweet_id = :tweetId AND account = :account LIMIT 1")
    internal abstract fun tweetExists(tweetId: Long, account: Int): Long?


    @Query("DELETE FROM favorite_tweets WHERE account = :account AND id <= (SELECT id FROM favorite_tweets WHERE account = :account ORDER BY tweet_id DESC LIMIT 1 OFFSET :trimSize)")
    internal abstract fun trimDatabase(account: Int, trimSize: Int)


}
