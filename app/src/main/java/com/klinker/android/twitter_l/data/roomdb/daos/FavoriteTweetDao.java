package com.klinker.android.twitter_l.data.roomdb.daos;


import android.database.Cursor;

import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteTweet;
import com.klinker.android.twitter_l.data.roomdb.pojos.TweetWithUserInfo;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class FavoriteTweetDao {

    @Insert
    abstract void insertFavoriteTweet(FavoriteTweet favoriteTweet);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract void insertFavoriteTweets(List<FavoriteTweet> favoriteTweets);


    @Delete
    abstract void deleteFavoriteTweet(FavoriteTweet favoriteTweet);

    @Query("DELETE FROM favorite_tweets WHERE account = :account AND tweet_id = :tweetId")
    abstract void deleteFavoriteTweet(long tweetId, int account);

    @Query("DELETE FROM favorite_tweets WHERE account = :account")
    abstract void deleteAllFavoriteTweets(int account);


    @Query("SELECT * FROM favorite_tweets JOIN tweets ON tweet_id = tweets.id AND account = :account JOIN users ON user_id = users.id ORDER BY tweet_id DESC LIMIT :timelineSize")
    abstract Cursor getFavoriteTweetsCursor(int account, int timelineSize);

    //list of tweets with user info


    @Query("SELECT tweet_id FROM favorite_tweets WHERE account = :account ORDER BY tweet_id DESC LIMIT 5")
    abstract long[] getLatestFavoriteTweetIds(int account);

    @Query("SELECT id FROM favorite_tweets WHERE tweet_id = :tweetId AND account = :account LIMIT 1")
    abstract Long tweetExists(long tweetId, int account);


    @Query("DELETE FROM favorite_tweets WHERE account = :account AND id <= (SELECT id FROM favorite_tweets WHERE account = :account ORDER BY tweet_id DESC LIMIT 1 OFFSET :trimSize)")
    abstract void trimDatabase(int account, int trimSize);


}
