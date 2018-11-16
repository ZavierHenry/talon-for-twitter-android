package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.UserTweet

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class UserTweetDao : TweetDao() {


    @Insert
    internal abstract fun insertUserTweet(userTweet: UserTweet)

    @Query("DELETE FROM user_tweets WHERE tweet_id = :tweetId")
    internal abstract fun deleteUserTweet(tweetId: Long)

    @Query("DELETE FROM user_tweets WHERE user_id = :userId")
    internal abstract fun deleteTweetsForUser(userId: Long)


    //possibly put an index on tweet_id column to improve performance
    @Query("DELETE FROM user_tweets WHERE user_id = :userId AND tweet_id < (SELECT MIN(tweet_id) FROM user_tweets WHERE user_id = :userId ORDER BY tweet_id DESC LIMIT :trimSize)")
    abstract fun trimDatabase(userId: Long, trimSize: Int)


}
