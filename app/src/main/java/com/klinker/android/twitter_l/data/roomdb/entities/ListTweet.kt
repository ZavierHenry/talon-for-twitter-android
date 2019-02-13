package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import twitter4j.Status

@Entity(tableName = "list_tweets",
        indices = [Index(value = ["list_id", "tweet_interaction_id"], unique = true)],
        foreignKeys = [ForeignKey(entity = TweetInteraction::class, parentColumns = ["id"], childColumns = ["tweet_interaction_id"], onDelete = ForeignKey.CASCADE)])
data class ListTweet(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                     @ColumnInfo(name = "tweet_interaction_id") val tweetInteractionId: Long,
                     @ColumnInfo(name = "list_id") val listId: Long)