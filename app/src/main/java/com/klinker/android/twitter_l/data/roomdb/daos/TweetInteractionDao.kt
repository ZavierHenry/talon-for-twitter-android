package com.klinker.android.twitter_l.data.roomdb.daos

import android.content.SharedPreferences
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.klinker.android.twitter_l.data.roomdb.entities.TweetInteraction
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayTweet


@Dao
abstract class TweetInteractionDao {

    @Update
    abstract fun updateTweetInteraction(interaction: TweetInteraction): Int

}