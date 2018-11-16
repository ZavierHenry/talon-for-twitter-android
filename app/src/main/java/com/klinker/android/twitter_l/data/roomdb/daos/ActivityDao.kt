package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.Activity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class ActivityDao {

    //0 - MENTION
    //1 - NEW_FOLLOWER
    //2 - RETWEETS
    //3 - FAVORITES


    @Insert
    internal abstract fun insertActivitySpecificInfo(activity: Activity)

    @Query("SELECT * FROM activities WHERE account = :account ORDER BY time ASC")
    abstract fun getAllActivities(account: Int): List<Activity>


    @Query("UPDATE activities SET user_id = :newUserId WHERE user_id = :oldUserId")
    internal abstract fun updateUserId(oldUserId: Long, newUserId: Long)

    @Query("SELECT tweet_id FROM activities WHERE account = :account ORDER BY time DESC LIMIT 2")
    abstract fun getLastTwoIds(account: Int): LongArray

    @Query("SELECT tweets.like_count FROM activities JOIN tweets ON activities.tweet_id = tweets.id AND tweet_id = :tweetId AND account = :account AND type = 3")
    internal abstract fun favoriteExists(tweetId: Long, account: Int): Int

    @Query("SELECT tweets.retweet_count FROM activities JOIN tweets ON tweet_id = tweets.id AND tweet_id = :tweetId AND account = :account AND type = 2")
    internal abstract fun retweetExists(tweetId: Long, account: Int): Int

    @Query("DELETE FROM activities WHERE account = :account")
    internal abstract fun deleteActivities(account: Int)


    @Query("DELETE FROM activities WHERE account = :account AND id NOT IN(SELECT id FROM activities WHERE account = :account ORDER BY time DESC LIMIT :trimSize)")
    abstract fun trimDatabase(account: Int, trimSize: Int)

}
