package com.klinker.android.twitter_l.data.roomdb.daos


import android.content.SharedPreferences

import com.klinker.android.twitter_l.data.roomdb.pojos.TweetWithUserInfo

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class HomeTweetDao : TweetDao() {


    @Transaction
    open fun markCurrentPosition(account: Int, id: Long) {
        removeCurrentPositionOtherTweets(account, id)
        saveCurrentPosition(account, id)
    }

    @Query("UPDATE home_tweets SET is_current_pos = 1 WHERE account = :account AND id = :id")
    internal abstract fun saveCurrentPosition(account: Int, id: Long)


    @Query("UPDATE home_tweets SET is_current_pos = 0 WHERE account = :account AND id <> :markedId")
    internal abstract fun removeCurrentPositionOtherTweets(account: Int, markedId: Long)


    @Query("DELETE FROM home_tweets WHERE account = :account AND id < (SELECT MIN(id) FROM home_tweets WHERE account = :account ORDER BY id DESC LIMIT :trimSize)")
    internal abstract fun trimDatabase(account: Int, trimSize: Int)



    //internal abstract fun getUnreadTweetsFilterUsersNoRetweets(account: Int, mutedUsers: List<String>, mutedClients: List<String>): List<TweetWithUserInfo>


//    @Query("SELECT * FROM home_tweets")
//    internal abstract fun getUnreadTweetsFilterUsersWithRetweets(account: Int, mutedUsers: List<String>, mutedClients: List<String>): List<TweetWithUserInfo>


}
