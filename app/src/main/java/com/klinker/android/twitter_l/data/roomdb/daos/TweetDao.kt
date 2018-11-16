package com.klinker.android.twitter_l.data.roomdb.daos


import android.content.Context
import androidx.room.*
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.entities.User

import twitter4j.Status

@Dao
abstract class TweetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    internal abstract fun insertTweet(tweet: Tweet)

    @Update
    abstract fun updateTweet(tweet: Tweet)

    @Transaction
    open fun insertTweet(context: Context, status: Status) {
        val user = User(if (status.isRetweet) status.retweetedStatus.user else status.user)
        val tweet = Tweet(status)

        TalonDatabase.getInstance(context).userDao().insertUser(user)
        insertTweet(tweet)

    }



    protected fun filterMutes(tweets: List<Tweet>, mutedHashtags: List<String>, mutedExpressions: List<String>): List<Tweet>? {
        return null
    }


}
