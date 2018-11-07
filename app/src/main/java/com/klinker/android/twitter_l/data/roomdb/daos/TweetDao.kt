package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.Tweet

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
abstract class TweetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract fun insertTweetSpecificData(tweet: Tweet)


    protected fun filterMutes(tweets: List<Tweet>, mutedHashtags: List<String>, mutedExpressions: List<String>): List<Tweet>? {
        return null
    }


}
