package com.klinker.android.twitter_l.data.roomdb.daos


import android.content.SharedPreferences
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayTweet

@Dao
abstract class HomeTweetDao {





    //TODO: deleteTweet

    @Query("DELETE FROM home_tweets WHERE account = :account")
    abstract fun deleteAllHomeTweets(account: Int)


    //TODO: get tweets

    //TODO: get wear tweets

    //TODO: get widget tweets

    //TODO: get unread tweets

    //TODO: get search tweets

    //TODO: get tweets with pictures

    //TODO: get tweets with links

    //TODO: get favorite users

    //TODO: get unread count

    //TODO: mark all read

    //TODO: mark unread filling

    //TODO: make get latest ids

    //TODO: make mark position

    //TODO: make get position of tweet id

    //TODO: make get current position of timeline

    //TODO: check if removeHTML is a necessary function

    @Query("DELETE FROM home_tweets WHERE account = :account AND id <= (SELECT id FROM home_tweets WHERE account = :account ORDER BY id DESC LIMIT 1 OFFSET :trimSize)")
    abstract fun trimDatabase(account: Int, trimSize: Int)


}
