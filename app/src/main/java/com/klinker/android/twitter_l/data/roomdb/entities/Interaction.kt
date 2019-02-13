package com.klinker.android.twitter_l.data.roomdb.entities

import android.content.Context

import com.klinker.android.twitter_l.R

import java.util.GregorianCalendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import twitter4j.Status

@Entity(tableName = "interactions",
        foreignKeys = [
            ForeignKey(entity = UserInteraction::class, parentColumns = ["id"], childColumns = ["tweet_interaction_id"], onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = TweetInteraction::class, parentColumns = ["id"], childColumns = ["user_interaction_id"], onDelete = ForeignKey.CASCADE)],
        indices = [Index(value = ["time"])])
data class Interaction(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                       @ColumnInfo(name = "tweet_interaction_id") val tweetInteractionId: Long? = null,
                       @ColumnInfo(name = "user_interaction_id") val userInteractionId: Long? = null,
                       @ColumnInfo val account: Int,
                       @ColumnInfo(name = "is_unread") val isUnread: Boolean,
                       @ColumnInfo val type: Int,
                       @ColumnInfo val title: String,
                       @ColumnInfo val time: Long) {


    companion object {

        const val FOLLOWER = 0
        const val RETWEET = 1
        const val FAVORITE = 2
        const val MENTION = 3
        const val FAVORITE_USER = 4
        const val QUOTED_TWEET = 5

    }

}
