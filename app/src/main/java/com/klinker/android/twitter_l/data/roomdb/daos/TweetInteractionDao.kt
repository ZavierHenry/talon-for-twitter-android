package com.klinker.android.twitter_l.data.roomdb.daos

import android.content.SharedPreferences
import androidx.room.*
import com.klinker.android.twitter_l.data.roomdb.entities.TweetInteraction
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayTweet


@Dao
abstract class TweetInteractionDao {

    @Insert
    abstract fun insertTweetInteraction(interaction: TweetInteraction): Long?

    @Update
    abstract fun updateTweetInteraction(interaction: TweetInteraction): Int



    @Query("UPDATE tweet_interactions SET is_saved = 0 WHERE account = :account")
    abstract fun deleteAllSavedTweets(account: Int)


    @Query("SELECT * FROM tweet_interactions WHERE account = :account AND is_saved = 1 ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getSavedTweets(account: Int, page: Int = 1, pageSize: Int = 1) : List<TweetInteraction>


    @Query("SELECT id FROM tweet_interactions WHERE account = :account AND tweet_id = :tweetId AND is_saved = 1")
    abstract fun getSavedTweetId(tweetId: Long, account: Int) : Long?


    fun isSavedTweet(tweetId: Long, account: Int) : Boolean {
        return getSavedTweetId(tweetId, account) != null
    }


    //-----------------------------------------------------------------------------------------------------------------------
    //Favorite Tweet functions



    @Query("SELECT * FROM tweet_interactions WHERE account = :account ORDER BY tweet_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getFavoriteTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<TweetInteraction>


    @Query("SELECT tweet_id FROM tweet_interactions WHERE account = :account AND is_liked = 1 ORDER BY tweet_id DESC LIMIT :limit")
    abstract fun getLatestFavoriteTweetIds(account: Int, limit: Int = 5): List<Long>


    @Query("UPDATE tweet_interactions SET is_liked = 0 WHERE account = :account AND is_liked = 1")
    abstract fun deleteAllFavoriteTweets(account: Int)




    //-----------------------------------------------------------------------------------------------------------------------


    @Query("DELETE FROM tweet_interactions WHERE account = :account")
    abstract fun deleteAllTweets(account: Int)


    @Transaction
    fun trimTweetInteractions(account: Int) {

    }


}